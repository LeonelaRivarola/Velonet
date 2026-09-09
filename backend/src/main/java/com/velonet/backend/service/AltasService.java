package com.velonet.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.dto.AltasComparativaDTO;

@Service
public class AltasService {

    private final RealSoftwareClient realSoftwareClient;

    public AltasService(RealSoftwareClient realSoftwareClient) {
        this.realSoftwareClient = realSoftwareClient;
    }

    public int getAltasEnPeriodo(LocalDate desde, LocalDate hasta) {

        DateTimeFormatter formatoApi = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");
        body.put("fecha_tipo", "c"); // fecha de creación
        body.put("fecha_desde", desde.format(formatoApi));
        body.put("fecha_hasta", hasta.format(formatoApi));
        body.put("cantidad", 1);

        Map<String, Object> respuesta = realSoftwareClient.post(body);

        if (respuesta == null || !respuesta.containsKey("resultados")) {
            return 0;
        }

        return Integer.parseInt(respuesta.get("resultados").toString());
    }

    public List<AltasComparativaDTO> getCurvaAltas(int mesesAtras) {

        List<AltasComparativaDTO> curva = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (int i = mesesAtras - 1; i >= 0; i--) {

            LocalDate mes = hoy.minusMonths(i);

            LocalDate desde = mes.withDayOfMonth(1);
            LocalDate hasta = mes.withDayOfMonth(mes.lengthOfMonth());

            int cantidad = getAltasEnPeriodo(desde, hasta);

            String periodo = desde.format(
                DateTimeFormatter.ofPattern("MM-yyyy")
            );

            curva.add(new AltasComparativaDTO(periodo, cantidad));
        }

        return curva;
    }
}