package com.velonet.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.dto.ReciboDTO;

@Service
public class RecibosService {
    private final RealSoftwareClient realSoftwareClient;

    public RecibosService(RealSoftwareClient realSoftwareClient) {
        this.realSoftwareClient = realSoftwareClient;
    }

    public List<ReciboDTO> getRecibos(String fechaDesde, String fechaHasta) {

        Map<String, Object> body = new HashMap<>();

        body.put("action", "recibos");
        body.put("fecha_desde", fechaDesde);
        body.put("fecha_hasta", fechaHasta);

        Map<String, Object> respuesta = realSoftwareClient.post(body);

        Map<String, Object> recibos = (Map<String, Object>) respuesta.get("recibos");

        // crear la lista vacia
        List<ReciboDTO> lista = new ArrayList<>();

        // recorrer lista de recibos
        for (Object value : recibos.values()) {
            // convertimos
            Map<String, Object> recibo = (Map<String, Object>) value;

            // scar fecha
            String fecha = recibo.get("fecha_recibo").toString();

            // sacar items
            Map<String, Object> items = (Map<String, Object>) recibo.get("items");

            // recorrer items
            double total = 0;
            for (Object item : items.values()) {
                Map<String, Object> detalle = (Map<String, Object>) item;
                // leer item
                String importe = detalle.get("importe").toString();
                total += Double.parseDouble(importe);
            }

            // crer dto
            lista.add(new ReciboDTO(fecha, total));
        }

        return lista;
    }
}
