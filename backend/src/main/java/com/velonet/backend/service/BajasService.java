package com.velonet.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.dto.BajasComparativaDTO;

@Service
public class BajasService {

    private final RealSoftwareClient realSoftwareClient;

    public BajasService(RealSoftwareClient realSoftwareClient) {
        this.realSoftwareClient = realSoftwareClient;
    }

    // Cuenta las bajas (estado 6) modificadas dentro de un rango de fechas
    public int getBajasEnPeriodo(LocalDate desde, LocalDate hasta) {
        DateTimeFormatter formatoApi = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");
        body.put("estado", "6");        // Baja
        body.put("fecha_tipo", "m");    // fecha de modificación (cuando pasó a Baja)
        body.put("fecha_desde", desde.format(formatoApi));
        body.put("fecha_hasta", hasta.format(formatoApi));
        body.put("cantidad", 1); // no necesitamos el detalle, solo "resultados"

        Map<String, Object> respuesta = realSoftwareClient.post(body);

        if (respuesta == null || !respuesta.containsKey("resultados")) {
            return 0;
        }

        return Integer.parseInt(respuesta.get("resultados").toString());
    }

    // Arma la curva de los últimos N meses (para el gráfico comparativo)
    public List<BajasComparativaDTO> getCurvaBajas(int mesesAtras) {
        List<BajasComparativaDTO> curva = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (int i = mesesAtras - 1; i >= 0; i--) {
            LocalDate mes = hoy.minusMonths(i);
            LocalDate desde = mes.withDayOfMonth(1);
            LocalDate hasta = mes.withDayOfMonth(mes.lengthOfMonth());

            int cantidad = getBajasEnPeriodo(desde, hasta);
            String periodo = desde.format(DateTimeFormatter.ofPattern("MM-yyyy"));

            curva.add(new BajasComparativaDTO(periodo, cantidad));
        }

        return curva;
    }
}