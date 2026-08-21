package com.velonet.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.velonet.backend.client.RealSoftwareClient;

@Service
public class ContratosService {

    private final RealSoftwareClient realSoftwareClient;

    public ContratosService(RealSoftwareClient realSoftwareClient) {
        this.realSoftwareClient = realSoftwareClient;
    }

    private Map<String, Object> obtenerContratos() {

        Map<String, Object> body = new HashMap<>();
        body.put("action", "contratos");
        body.put("incluye_bajas", "N");

        return realSoftwareClient.post(body);
    }

    public long getTotalContratosActivos() {

        long totalContratosActivos = 0;
        Map<String, Object> respuesta = obtenerContratos();

        if (respuesta.containsKey("resultados")) {
            totalContratosActivos = Long.parseLong(respuesta.get("resultados").toString());
        }

        if (respuesta.containsKey("cantidad")) {
            totalContratosActivos = Long.parseLong(respuesta.get("cantidad").toString());
        }

        return totalContratosActivos;
    }

    @SuppressWarnings("unchecked")
    public long getContratosBonificados() {

        long contratosSinCobro = 0;

        Map<String, Object> respuesta = obtenerContratos();

        if (respuesta.containsKey("contratos")) {

            Object obj = respuesta.get("contratos");

            if (obj instanceof List<?>) {

                List<Map<String, Object>> lista = (List<Map<String, Object>>) obj;

                for (Map<String, Object> contrato : lista) {

                    String obs = (String) contrato.get("observaciones");

                    if (obs != null &&
                            obs.toLowerCase().contains("bonificado")) {

                        contratosSinCobro++;
                    }
                }
            }
        }

        return contratosSinCobro;
    }
}