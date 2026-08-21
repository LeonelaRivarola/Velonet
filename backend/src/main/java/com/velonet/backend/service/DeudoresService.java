package com.velonet.backend.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.entity.HistorialDeudores;
import com.velonet.backend.repository.HistorialDeudoresRepository;

@Service
public class DeudoresService {
    
    private final RealSoftwareClient realSoftwareClient;
    private final HistorialDeudoresRepository repository;

    public DeudoresService(RealSoftwareClient realSoftwareClient, HistorialDeudoresRepository repository){
        this.realSoftwareClient = realSoftwareClient;
        this.repository = repository;
    }

    public int getCantidadDeudoresActual(){

        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");
        body.put("estado", "2");
        body.put("cantidad", 100);

        int offset = 0;
        int resultados = Integer.MAX_VALUE;
        int totalDeudores = 0;

        while (offset < resultados){

            body.put("offset", offset);
            Map<String, Object> respuesta = realSoftwareClient.post(body);
            
            resultados = Integer.parseInt(
                    respuesta.get("resultados").toString());

            Map<String, Object> clientes =
                    (Map<String, Object>) respuesta.get("clientes");

            if (clientes == null || clientes.isEmpty()) {
                break;
            }

            totalDeudores += clientes.size();

            System.out.println("Offset: " + offset +
                    " - Deudores: " + clientes.size() +
                    " - Total API: " + resultados);

            offset += 100;
        }

        return totalDeudores;
    }

    public void guardarDeudoresHoy(){
        LocalDate hoy = LocalDate.now();

        if(repository.existsByFecha(hoy)){
            return;
        }

        int cantidad = getCantidadDeudoresActual();
        HistorialDeudores registro = new HistorialDeudores(hoy, cantidad);
        repository.save(registro);
        System.out.println("Deudores guardado: " + cantidad);
    }
}
