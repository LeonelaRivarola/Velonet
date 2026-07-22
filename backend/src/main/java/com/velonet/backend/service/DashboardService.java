package com.velonet.backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.dto.DashboardMetricsDTO;

@Service
public class DashboardService {

    private RealSoftwareClient realSoftwareClient;

    public DashboardService(RealSoftwareClient realSoftwareClient) {
        this.realSoftwareClient = realSoftwareClient;
    }

    // ============================
    // DashboardDTO dto = new DashboardDTO();

    // dto.setClientes(clientesService.totalClientes());

    // dto.setContratos(contratosService.totalContratos());

    // dto.setCurvaPagos(curvaPagosService.obtener(...));

    // dto.setCurvaDeudores(curvaDeudoresService.obtener(...));

    // return dto;

    // ============================

    @SuppressWarnings("unchecked")
    public DashboardMetricsDTO getConsolidatedMetrics() {
        // HttpHeaders headers = createAuthHeaders();

        long totalContratosActivos = 0;
        long contratosSinCobro = 0;
        long totalClientesActivos = 0;

        // 1. PETICIÓN Y CÁLCULO DE CONTRATOS
        try {
            Map<String, Object> contractBody = new HashMap<>();
            contractBody.put("action", "contratos");
            contractBody.put("incluye_bajas", "N");

            Map<String, Object> rootContratos = realSoftwareClient.post(contractBody);
            // System.out.println(rootContratos);
            if (rootContratos != null) {

                if (rootContratos.containsKey("resultados")) {
                    totalContratosActivos = Long.parseLong(rootContratos.get("resultados").toString());
                } else if (rootContratos.containsKey("cantidad")) {
                    totalContratosActivos = ((Number) rootContratos.get("cantidad")).longValue();
                }

                if (rootContratos.containsKey("contratos") && rootContratos.get("contratos") instanceof List) {

                    List<Map<String, Object>> contratosList = (List<Map<String, Object>>) rootContratos
                            .get("contratos");

                    for (Map<String, Object> c : contratosList) {
                        String obs = (String) c.get("observaciones");

                        if (obs != null && obs.toLowerCase().contains("bonificado")) {
                            contratosSinCobro++;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. PETICIÓN Y CÁLCULO DE CLIENTES
        try {
            Map<String, Object> clientBody = new HashMap<>();
            clientBody.put("action", "clientes_consulta");
            clientBody.put("estado", "1");

            Map<String, Object> rootClientes = realSoftwareClient.post(clientBody);

            // System.out.println(rootClientes.keySet());

            // System.out.println("cantidad = " + rootClientes.get("cantidad"));
            // System.out.println("resultados = " + rootClientes.get("resultados"));
            // System.out.println("clientes = " + rootClientes.get("clientes"));

            if (rootClientes != null) {
                System.out.println(rootClientes.get("resultados").getClass());

                if (rootClientes.containsKey("resultados")) {
                    // System.out.println("ENTRÓ AL IF");
                    totalClientesActivos = Long.parseLong(rootClientes.get("resultados").toString());
                    // System.out.println("TOTAL = " + totalClientesActivos);
                } else if (rootClientes.containsKey("cantidad")) {
                    totalClientesActivos = ((Number) rootClientes.get("cantidad")).longValue();
                } else if (rootClientes.containsKey("clientes")) {

                    Object clientesObj = rootClientes.get("clientes");
                    if (clientesObj instanceof List) {
                        totalClientesActivos = ((List<?>) clientesObj).size();
                    } else if (clientesObj instanceof Map) {
                        totalClientesActivos = ((Map<?, ?>) clientesObj).size();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DashboardMetricsDTO(totalClientesActivos, totalContratosActivos, contratosSinCobro);
    }
}