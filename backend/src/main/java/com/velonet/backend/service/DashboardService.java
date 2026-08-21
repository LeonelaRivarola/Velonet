package com.velonet.backend.service;

import org.springframework.stereotype.Service;
import com.velonet.backend.dto.DashboardMetricsDTO;

@Service
public class DashboardService {

    private final ClienteService clienteService;
    private final ContratosService contratosService;

    public DashboardService(ClienteService clienteService, ContratosService contratosService) {
        this.clienteService = clienteService;
        this.contratosService = contratosService;
    }

    public DashboardMetricsDTO getConsolidatedMetrics(){
        return new DashboardMetricsDTO(
            clienteService.getTotalClientesActivos(),
            contratosService.getTotalContratosActivos(),
            contratosService.getContratosBonificados()
        );
    }
}