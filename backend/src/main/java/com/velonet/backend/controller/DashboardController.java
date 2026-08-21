package com.velonet.backend.controller;

import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.velonet.backend.dto.CurvaPagoMensualDTO;
import com.velonet.backend.dto.DashboardMetricsDTO;
import com.velonet.backend.dto.DeudoresComparativaDTO;
import com.velonet.backend.entity.HistorialCuentaCorriente;
import com.velonet.backend.service.CuentaCorrienteService;
import com.velonet.backend.service.CurvaPagosService;
import com.velonet.backend.service.DashboardService;
import com.velonet.backend.service.DeudoresHistoricosService;
import com.velonet.backend.service.DeudoresService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CurvaPagosService curvaPagosService;
    private final DeudoresHistoricosService deudoresHistoricoService;
    private final DeudoresService deudoresService;
    private final CuentaCorrienteService cuentaCorrienteService;

    public DashboardController(DashboardService dashboardService, CurvaPagosService curvaPagosService,
            DeudoresHistoricosService deudoresHistoricosService, DeudoresService deudoresService,
            CuentaCorrienteService cuentaCorrienteService) {
        this.dashboardService = dashboardService;
        this.curvaPagosService = curvaPagosService;
        this.deudoresHistoricoService = deudoresHistoricosService;
        this.deudoresService = deudoresService;
        this.cuentaCorrienteService = cuentaCorrienteService;

    }

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDTO> getMetrics() {
        return ResponseEntity.ok(dashboardService.getConsolidatedMetrics());
    }

    @GetMapping("/pagos")
    public ResponseEntity<List<CurvaPagoMensualDTO>> getCurvaPagos(@RequestParam(defaultValue = "60") int periodo) {

        List<CurvaPagoMensualDTO> curva = curvaPagosService.getCurvaPagos(periodo);

        return ResponseEntity.ok(curva);
    }

    @GetMapping("/deudores")
    public ResponseEntity<List<DeudoresComparativaDTO>> getDeudores() {
        return ResponseEntity.ok(deudoresHistoricoService.obtenerHistorico());
    }

    @GetMapping("/deudores/snapshot")
    public ResponseEntity<String> generarSnapshot() {
        deudoresService.guardarDeudoresHoy();

        return ResponseEntity.ok("Snapshot generado");
    }

    // @GetMapping("/saldo-cliente")
    // public ResponseEntity<Double> getSaldoCliente(@RequestParam Long clienteId) {
    // double saldo = cuentaCorrienteService.getSaldoCorriente(clienteId);
    // return ResponseEntity.ok(saldo);
    // }

    @GetMapping("/cuenta-corriente/{clienteId}")
    public ResponseEntity<Double> getSaldoCuentaCorriente(@PathVariable Long clienteId) {
        double saldo = cuentaCorrienteService.getSaldoCorriente(clienteId);
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/probar-cuenta-corriente")
    public ResponseEntity<Double> probarCuentaCorriente() {
        double saldoTotal = cuentaCorrienteService.probarClientes();
        return ResponseEntity.ok(saldoTotal);
    }

    // @GetMapping("/deuda-total-cuenta-corriente")
    // public ResponseEntity<Double> getDeudaTotalCuentaCorriente() {
    //     double deudaTotal = cuentaCorrienteService.getDeudaTotalCuentaCorriente();
    //     return ResponseEntity.ok(deudaTotal);
    // }

    @GetMapping("/cuenta-corriente/snapshot")
    public ResponseEntity<String> generarSnapshotCuentaCorriente() {
        cuentaCorrienteService.guardarDeudaHoy();
        return ResponseEntity.ok("Snapshot de deuda total de cuenta corriente generado");
    }

    @GetMapping("/cuenta-corriente/historial")
public ResponseEntity<List<HistorialCuentaCorriente>> getHistorialCuentaCorriente() {
    return ResponseEntity.ok(cuentaCorrienteService.getHistorial());
}
}
