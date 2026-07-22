package com.velonet.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.velonet.backend.dto.CurvaPagoMensualDTO;
import com.velonet.backend.dto.DashboardMetricsDTO;
import com.velonet.backend.service.CurvaPagosService;
import com.velonet.backend.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CurvaPagosService curvaPagosService;
    

    public DashboardController(DashboardService dashboardService, CurvaPagosService curvaPagosService) {
        this.dashboardService = dashboardService;
        this.curvaPagosService = curvaPagosService;
    }

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDTO> getMetrics() {
        return ResponseEntity.ok(dashboardService.getConsolidatedMetrics());
    }

    @GetMapping("/pagos")
    public ResponseEntity<List<CurvaPagoMensualDTO>> getCurvaPagos(@RequestParam String fechaDesde, @RequestParam String fechaHasta) {
        List<CurvaPagoMensualDTO> curva = curvaPagosService.getCurvaPagos(fechaDesde, fechaHasta);
        return ResponseEntity.ok(curva);
    }

    // @GetMapping("/deudores")
    // @GetMapping("/kpis")

}
