package com.velonet.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.velonet.backend.dto.CurvaPagoMensualDTO;
import com.velonet.backend.dto.ReciboApiDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

@Service
public class CurvaPagosService {
    
    private final RestTemplate restTemplate;

    @Value("${realsoftware.api.url}")
    private String apiUrl;

    public CurvaPagosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CurvaPagoMensualDTO> getCurvaPagos(String fechaDesde, String fechaHasta) {
        // Implementa la lógica para obtener la curva de pagos desde la API de Real Software
        // Por ejemplo, podrías hacer una solicitud GET a un endpoint específico de la API
        // y mapear la respuesta a una lista de ReciboApiDTO.
        String url = String.format("%s?action=recibos&fecha_desde=%s&fecha_hasta=%s", apiUrl, fechaDesde,fechaHasta);

        //consumir api
        ResponseEntity<ReciboApiDTO[]> response = restTemplate.getForEntity(url, ReciboApiDTO[].class);
        ReciboApiDTO[] recibos = response.getBody();

        if(recibos == null || recibos.length == 0) {
            return Collections.emptyList(); // Retorna una lista vacía si no hay recibos
        }

        //procesoy agrupo por mes/año
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Map<String, BigDecimal> pagosMensuales = Arrays.stream(recibos)
            .collect(Collectors.groupingBy(
                recibo -> {
                    LocalDate fecha = LocalDate.parse(recibo.getFecha(), formatter);
                    return fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue()); // Devuelve "2024-01"
                },
                Collectors.reducing(
                    BigDecimal.ZERO,
                    ReciboApiDTO::getImporte,
                    BigDecimal::add
                )
            ));

            //Convertir el map en lista de dtos para el front y ordeno por fecha
            return pagosMensuales.entrySet().stream()
                .map(entry -> new CurvaPagoMensualDTO(entry.getKey(), entry.getValue()))
                .sorted((dto1, dto2) -> dto1.getMesAnio().compareTo(dto2.getMesAnio()))
                .collect(Collectors.toList());
    }
}
