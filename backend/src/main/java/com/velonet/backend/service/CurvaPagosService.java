package com.velonet.backend.service;

import org.springframework.stereotype.Service;

import com.velonet.backend.dto.CurvaPagoMensualDTO;
import com.velonet.backend.dto.ReciboDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurvaPagosService {

    private final RecibosService recibosService;

    public CurvaPagosService(RecibosService recibosService) {
        this.recibosService = recibosService;
    }

    public List<CurvaPagoMensualDTO> getCurvaPagos(String fechaDesde, String fechaHasta) {

        List<ReciboDTO> recibos = recibosService.getRecibos(fechaDesde, fechaHasta);

        // procesoy agrupo por mes/año
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Map<String, Double> pagosMensuales = recibos.stream()
                .collect(Collectors.groupingBy(
                        recibo -> {
                            LocalDate fecha = LocalDate.parse(recibo.getFecha(), formatter);
                            
                            return fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue()); // Devuelve
                                                                                                         // "2024-01"
                        },
                        Collectors.summingDouble(ReciboDTO::getImporte)
                    ));

        // Convertir el map en lista de dtos para el front y ordeno por fecha
        return pagosMensuales.entrySet().stream()
                .map(entry -> new CurvaPagoMensualDTO(entry.getKey(), entry.getValue()))
                .sorted((dto1, dto2) -> dto1.getMesAnio().compareTo(dto2.getMesAnio()))
                .collect(Collectors.toList());
    }
}
