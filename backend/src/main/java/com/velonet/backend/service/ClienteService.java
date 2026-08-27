package com.velonet.backend.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    body.put("action", "contratos");
    body.put("tipo_contrato", "1");   // Contrato modelo
    body.put("estado_cliente", "1");  // Cliente Activo

    Set<String> clientesUnicos = new HashSet<>();
    int offset = 0;
    int resultados = Integer.MAX_VALUE;

    while (offset < resultados) {
        body.put("offset", offset);

        Map<String, Object> respuesta = realSoftwareClient.post(body);

        if (respuesta == null || !respuesta.containsKey("contratos")) {
            break;
        }

        resultados = Integer.parseInt(respuesta.get("resultados").toString());

        List<Map<String, Object>> contratos = (List<Map<String, Object>>) respuesta.get("contratos");
        if (contratos == null || contratos.isEmpty()) {
            break;
        }

        for (Map<String, Object> contrato : contratos) {
            // Solo contamos el contrato modelo si está VIGENTE
            String estado = (String) contrato.get("estado");
            if ("Vigente".equalsIgnoreCase(estado)) {
                clientesUnicos.add((String) contrato.get("cliente_id"));
            }
        }

        offset += contratos.size(); // avanza según lo recibido, no asume 100 fijo
    }

    return clientesUnicos.size();
}

    // public long getTotalClientesActivos() {

    //     Map<String, Object> body = new HashMap<>();
    //     body.put("action", "clientes_consulta");
    //     body.put("estado", "1");

    //     Map<String, Object> respuesta = realSoftwareClient.post(body);

    //     if (respuesta.containsKey("resultados")) {
    //         return Long.parseLong(respuesta.get("resultados").toString());
    //     }

    //     return 0;
    // }

    public Map<String, Object> getClientes(){

        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");

        return realSoftwareClient.post(body);

    }
}
