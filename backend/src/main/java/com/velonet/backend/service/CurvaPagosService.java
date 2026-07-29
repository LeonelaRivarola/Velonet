package com.velonet.backend.service;

import org.springframework.stereotype.Service;

import com.velonet.backend.dto.CurvaPagoMensualDTO;
import com.velonet.backend.dto.ReciboDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurvaPagosService {

    private final RecibosService recibosService;

    public CurvaPagosService(RecibosService recibosService) {
        this.recibosService = recibosService;
    }

    public List<CurvaPagoMensualDTO> getCurvaPagos(int periodo) {

        int meses;

        switch (periodo) {
            case 60:
                meses = 2;
                break;

            case 90:
                meses = 3;
                break;

            case 365:
                meses = 12;
                break;

            default:
                meses = 2;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        int diaCorte = 15;

        List<CurvaPagoMensualDTO> resultado = new ArrayList<>();

        for (int i = meses - 1; i >= 0; i--) {

            LocalDate mes = LocalDate.now().minusMonths(i);

            LocalDate fechaDesde = mes.withDayOfMonth(1);

            int ultimoDia = mes.lengthOfMonth();

            LocalDate fechaHasta = mes.withDayOfMonth(
                    Math.min(diaCorte, ultimoDia));

            List<ReciboDTO> recibos = recibosService.getRecibos(
                    fechaDesde.format(formatter),
                    fechaHasta.format(formatter));

            double total = 0;

            for (ReciboDTO recibo : recibos) {
                total += recibo.getImporte();
            }
            resultado.add(
                    new CurvaPagoMensualDTO(
                            mes.format(DateTimeFormatter.ofPattern("MM-yyyy")),
                            total));
        }

        return resultado;
    }
}
