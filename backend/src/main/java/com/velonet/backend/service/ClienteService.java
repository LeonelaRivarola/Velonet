package com.velonet.backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;

@Service
public class ClienteService {

    private final RealSoftwareClient realSoftwareClient;

    public ClienteService(RealSoftwareClient realSoftwareClient) {
        this.realSoftwareClient = realSoftwareClient;
    }

    public long getTotalClientesActivos() {

        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");
        body.put("estado", "1");

        Map<String, Object> respuesta = realSoftwareClient.post(body);

        if (respuesta.containsKey("resultados")) {
            return Long.parseLong(respuesta.get("resultados").toString());
        }

        return 0;
    }

    public Map<String, Object> getClientes(){

        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");

        return realSoftwareClient.post(body);

    }
}
