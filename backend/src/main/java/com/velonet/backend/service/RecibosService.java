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
        body.put("fecha_tipo", "R");
        body.put("cantidad", 100);

        // crear la lista vacia
        // Map<String, Double> totalesPorMes = new HashMap<>();
        List<ReciboDTO> lista = new ArrayList<>();

        int offset = 0;
        int resultados = Integer.MAX_VALUE;

        // recorrer lista de recibos
        while (offset < resultados ) {

            body.put("offset",offset);

            Map<String, Object> respuesta = realSoftwareClient.post(body);
            System.out.println("RESPUESTA COMPLETA: " + respuesta);
            
            resultados = Integer.parseInt(respuesta.get("resultados").toString());

            Map<String, Object> recibos = (Map<String, Object>) respuesta.get("recibos");

            if(recibos == null || recibos.isEmpty()){
                break;
            }

            System.out.println("Offset: " + offset + 
                " - Recibos: " + recibos.size() +
                " - Total API: " + resultados
            );

            for(Map.Entry<String, Object> entry: recibos.entrySet()){

                Map<String, Object> recibo = (Map<String, Object>) entry.getValue();

                String fecha = recibo.get("fecha_recibo").toString();

                Map<String, Object> items = (Map<String, Object>) recibo.get("items");
                double total = 0;

                for (Object item : items.values()) {
                    Map<String, Object> detalle = (Map<String, Object>) item;
                    total += Double.parseDouble(detalle.get("importe").toString());
                }

                // // agrupamos por mes
                // String mes = fecha.substring(3, 10); // MM-YYYY
                // totalesPorMes.merge(
                //     mes,
                //     total,
                //     Double::sum
                // );

                lista.add(
                    new ReciboDTO(fecha, total)
                );
            }
            
            offset += 100;
        }

        // List<ReciboDTO> lista = new ArrayList<>();

        // totalesPorMes.entrySet()
        //     .stream()
        //     .sorted(Map.Entry.comparingByKey())
        //     .forEach(entry ->
        //         lista.add(
        //             new ReciboDTO(entry.getKey()
        //             , entry.getValue()
        //         )
        //     )
        // );

        return lista;

    }
}
