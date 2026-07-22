package com.velonet.backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;

@Service
public class RecibosService {
    private final RealSoftwareClient realSoftwareClient;

    public RecibosService(RealSoftwareClient realSoftwareClient){
        this.realSoftwareClient = realSoftwareClient;
    }

    public Map<String, Object> obtenerRecibos(String fechaDesde, String fechaHasta){

        Map<String, Object> body = new HashMap<>();

        body.put("action", "recibos");
        body.put("fecha_desde", fechaDesde);
        body.put("fecha_hasta", fechaHasta);

        return realSoftwareClient.post(body);
    }
}
