package com.velonet.backend.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.entity.HistorialCuentaCorriente;
import com.velonet.backend.repository.HistorialCuentaCorrienteRepository;

@Service
public class CuentaCorrienteService {
    private final RealSoftwareClient realSoftwareClient;
    private  final HistorialCuentaCorrienteRepository repository;

    public CuentaCorrienteService(RealSoftwareClient realSoftwareClient, HistorialCuentaCorrienteRepository historialCuentaCorrienteService) {
        this.realSoftwareClient = realSoftwareClient;
        this.repository = historialCuentaCorrienteService;
    }

    public double getSaldoCorriente(Long clienteId) {
        Map<String, Object> body = new HashMap<>();
        body.put("action", "ctacte");
        body.put("cli_id", clienteId);

        Map<String, Object> response = realSoftwareClient.post(body);

        List<Map<String, Object>> comprobantes = (List<Map<String, Object>>) response.get("cmps");

        double total = 0;

        if (comprobantes != null) {
            for (Map<String, Object> comprobante : comprobantes) {
                Number saldo = (Number) comprobante.get("saldo");

                if (saldo != null) {
                    total += saldo.doubleValue();
                }
            }
        }

        return total;
    }

    // deuda total de los clientes
    // public double getSaldoTotalCC() {

    //     Map<String, Object> body = new HashMap<>();
    //     body.put("action", "cliente_consulta");
    //     body.put("cantidad", 100);

    //     int offset = 0;
    //     int resultados = Integer.MAX_VALUE;

    //     double totalGeneral = 0;

    //     while (offset < resultados) {

    //         body.put("offset", offset);

    //         Map<String, Object> respuesta = realSoftwareClient.post(body);

    //         resultados = Integer.parseInt(respuesta.get("resultados").toString());

    //         Map<String, Object> clientes = (Map<String, Object>) respuesta.get("clientes");
    //         if (clientes == null || clientes.isEmpty()) {
    //             break;
    //         }

    //         // recorre los clientes de esa pagina
    //         for (String clienteId : clientes.keySet()) {
    //             double saldoCliente = getSaldoCorriente(Long.parseLong(clienteId));
    //             totalGeneral += saldoCliente;
    //         }

    //         offset += 100;

    //     }

    //     return totalGeneral;
    // }

    public double getDeudaTotalCuentaCorriente() {
        Map<String, Object> body = new HashMap<>();

        body.put("action", "clientes_consulta");
        body.put("cantidad", 100);

        int offset = 0;
        int resultados = Integer.MAX_VALUE;
        double totalDeuda = 0;

        while (offset < resultados) {

            body.put("offset", offset);

            Map<String, Object> respuesta = realSoftwareClient.post(body);

            resultados = Integer.parseInt(respuesta.get("resultados").toString());

            Map<String, Object> clientes = (Map<String, Object>) respuesta.get("clientes");

            if (clientes == null || clientes.isEmpty()) {
                break;
            }

            // recorre los clientes de esa pagina
            for (String clienteId : clientes.keySet()) {
                double saldo = getSaldoCorriente(Long.parseLong(clienteId));
                totalDeuda += saldo;
            }

            offset += 100;
            System.out.println("Procesados: " + offset + " / " + resultados + " - Deuda acumulada: " + totalDeuda);

        }
        return totalDeuda;
    }

    @Async
    public void guardarDeudaHoy(){
        LocalDate hoy = LocalDate.now();

        if(repository.existsByFecha(hoy)){
            return;
        }

        double monto = getDeudaTotalCuentaCorriente();
        HistorialCuentaCorriente registro = new HistorialCuentaCorriente(hoy, monto);
        repository.save(registro);
        System.out.println("Registro de deuda guardado: " + monto);
    }

    @Scheduled(cron = "0 0 3 * * *") // 3 AM todos los días
public void snapshotDiarioCuentaCorriente() {
    guardarDeudaHoy(); 
}

public List<HistorialCuentaCorriente> getHistorial() {
    return repository.findTop10ByOrderByFechaDesc();
}
    // DEBUG / PRUEBA
public double probarClientes() {
    Map<String, Object> body = new HashMap<>();
    body.put("action", "clientes_consulta");
    body.put("cantidad", 6);

    Map<String, Object> respuesta = realSoftwareClient.post(body);
    Map<String, Object> clientes = (Map<String, Object>) respuesta.get("clientes");

    double total = 0;
    for (String clienteId : clientes.keySet()) {
        double saldo = getSaldoCorriente(Long.parseLong(clienteId));
        System.out.println("Cliente " + clienteId + " saldo " + saldo);
        total += saldo;
    }
    return total;
}
}
