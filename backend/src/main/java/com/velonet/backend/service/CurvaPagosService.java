package com.velonet.backend.service;

import org.springframework.stereotype.Service;

import com.velonet.backend.dto.CurvaPagoMensualDTO;
import com.velonet.backend.dto.ReciboDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurvaPagosService {

    private final RecibosService recibosService;

    public CurvaPagosService(RecibosService recibosService) {
        this.recibosService = recibosService;
    }

    public List<CurvaPagoMensualDTO> getCurvaPagos(String fechaDesde, String fechaHasta) {

        List<ReciboDTO> recibos = recibosService.getRecibos(fechaDesde, fechaHasta);

        return recibos.stream()
            .map(recibo -> new CurvaPagoMensualDTO(recibo.getFecha(), recibo.getImporte()))
            .sorted((a, b) -> {
                //separamos por - o / y armo yyyymm parar ordenar 
                String[] parteA = a.getMesAnio().split("[-/]");
                String[] parteB = a.getMesAnio().split("[-/]");
                String anioMesA = parteA[1] + parteA[0];
                String anioMesB = parteB[1] + parteB[0];
                return anioMesA.compareTo(anioMesB);

            })
            .collect(Collectors.toList());
    }
}
