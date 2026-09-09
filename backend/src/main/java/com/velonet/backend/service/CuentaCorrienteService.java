package com.velonet.backend.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.velonet.backend.client.RealSoftwareClient;
import com.velonet.backend.entity.HistorialCuentaCorriente;
import com.velonet.backend.repository.HistorialCuentaCorrienteRepository;

@Service
public class CuentaCorrienteService {
    private final RealSoftwareClient realSoftwareClient;
    private final HistorialCuentaCorrienteRepository repository;
    // para controlar la ejecucion simultanea
    private final AtomicBoolean procesandoSnapshot = new AtomicBoolean(false);
    // pool de 15 hilos concurrentes para peticiones http para reducir tiempo (1.5
    // hs)
    private final ExecutorService executor = Executors.newFixedThreadPool(15);

    public CuentaCorrienteService(RealSoftwareClient realSoftwareClient,
            HistorialCuentaCorrienteRepository historialCuentaCorrienteService) {
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

    // Map<String, Object> body = new HashMap<>();
    // body.put("action", "cliente_consulta");
    // body.put("cantidad", 100);

    // int offset = 0;
    // int resultados = Integer.MAX_VALUE;

    // double totalGeneral = 0;

    // while (offset < resultados) {

    // body.put("offset", offset);

    // Map<String, Object> respuesta = realSoftwareClient.post(body);

    // resultados = Integer.parseInt(respuesta.get("resultados").toString());

    // Map<String, Object> clientes = (Map<String, Object>)
    // respuesta.get("clientes");
    // if (clientes == null || clientes.isEmpty()) {
    // break;
    // }

    // // recorre los clientes de esa pagina
    // for (String clienteId : clientes.keySet()) {
    // double saldoCliente = getSaldoCorriente(Long.parseLong(clienteId));
    // totalGeneral += saldoCliente;
    // }

    // offset += 100;

    // }

    // return totalGeneral;
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

            if (respuesta == null || !respuesta.containsKey("resultados")) {
                break;
            }

            resultados = Integer.parseInt(respuesta.get("resultados").toString());
            Map<String, Object> clientes = (Map<String, Object>) respuesta.get("clientes");

            if (clientes == null || clientes.isEmpty()) {
                break;
            }

            // tareas asincronas por cada cliente del lote
            List<CompletableFuture<Double>> tareasSaldos = clientes.keySet().stream()
                    .map(clienteIdStr -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return getSaldoCorriente(Long.parseLong(clienteIdStr));
                        } catch (Exception e) {
                            System.err.println(
                                    "Error obteniendo saldo del cliente " + clienteIdStr + ": " + e.getMessage());
                            return 0.0;
                        }
                    }, executor))
                    .toList();

            // Esperar que el lode de los 100 complete su peticion
            CompletableFuture<Void> todasLasTareas = CompletableFuture
                    .allOf(tareasSaldos.toArray(new CompletableFuture[0]));

            todasLasTareas.join();// se bloquea hasta que las 100 llamadas finalicen

            // Sumar los resultlados del lote
            double sumaLote = tareasSaldos.stream().mapToDouble(CompletableFuture::join).sum();

            // recorre los clientes de esa pagina
            // for (String clienteId : clientes.keySet()) {
            // double saldo = getSaldoCorriente(Long.parseLong(clienteId));
            // totalDeuda += saldo;
            // }

            totalDeuda += sumaLote;
            offset += 100;
            System.out.println("Procesados: " + offset + " / " + resultados + " - Deuda acumulada: " + totalDeuda);

        }
        return totalDeuda;

    }

    @Async
    public void guardarDeudaHoy() {
        LocalDate hoy = LocalDate.now();

        if (repository.existsByFecha(hoy)) {
            System.out.println("El snapshot para la fecha " + hoy + " ya fue procesado.");
            return;
        }

        // Intenta cambiar de false a true. Si ya era true, retorna false y cancela la
        // nueva ejecución.
        if (!procesandoSnapshot.compareAndSet(false, true)) {
            System.out.println("Ya existe un proceso de snapshot en ejecución.");
            return;
        }

        try {
            System.out.println("Iniciando snapshot de deuda total de cuenta corriente...");
            long inicio = System.currentTimeMillis();

            double monto = getDeudaTotalCuentaCorriente();
            HistorialCuentaCorriente registro = new HistorialCuentaCorriente(hoy, monto);
            repository.save(registro);

            long duracionSegundos = (System.currentTimeMillis() - inicio) / 1000;
            System.out.println(
                    "Snapshot guardado exitosamente: $" + monto + " (Tiempo: " + duracionSegundos + " segundos)");
        } catch (Exception e) {
            System.err.println("Error al guardar el snapshot de deuda: " + e.getMessage());
        } finally {
            // se libera unflag
            procesandoSnapshot.set(false);
        }
    }

    @Scheduled(cron = "0 0 3 * * *") // 3 AM todos los días
    public void snapshotDiarioCuentaCorriente() {
        guardarDeudaHoy();
    }

    public List<HistorialCuentaCorriente> getHistorial() {
        return repository.findTop10ByOrderByFechaDesc();
    }

    public List<Map<String, Object>> getVencimientos() {
        Map<String, Object> body = new HashMap<>();
        body.put("action", "clientes_consulta");
        body.put("estado", "1");
        body.put("cantidad", 100);

        List<Map<String, Object>> vencimientos = new java.util.ArrayList<>();

        int offset = 0;
        int resultados = Integer.MAX_VALUE;

        while (offset < resultados) {
            body.put("offset", offset);

            Map<String, Object> respuesta = realSoftwareClient.post(body);
            System.out.println("RESPUESTA CLIENTES VENCIMIENTOS: " + respuesta);

            if (respuesta == null || !respuesta.containsKey("resultados")) {
                break;
            }

            resultados = Integer.parseInt(respuesta.get("resultados").toString());
            Map<String, Object> clientes = (Map<String, Object>) respuesta.get("clientes");

            if (clientes == null || clientes.isEmpty()) {
                break;
            }

            List<CompletableFuture<Map<String, Object>>> tareas = clientes.keySet().stream()
                    .map(clienteIdStr -> CompletableFuture.supplyAsync(() -> {
                        try {
                            Long clienteId = Long.parseLong(clienteIdStr);
                            Map<String, Object> factura = getUltimaFactura(clienteId);

                            if (!factura.isEmpty()) {
                                factura.put("clienteId", clienteId);
                                return factura;
                            }

                        } catch (Exception e) {
                            System.err.println(
                                    "Error obteniendo factura del cliente " + clienteIdStr + ": " + e.getMessage());
                        }

                        return null;
                    }, executor))
                    .toList();

            CompletableFuture.allOf(
                    tareas.toArray(new CompletableFuture[0])).join();

            for (CompletableFuture<Map<String, Object>> tarea : tareas) {
                Map<String, Object> factura = tarea.join();

                if (factura != null) {
                    vencimientos.add(factura);
                }
            }
            offset += 100;
        }
        return vencimientos;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUltimaFactura(Long clienteId) {

        Map<String, Object> body = new HashMap<>();
        body.put("action", "ctacte");
        body.put("cli_id", clienteId);

        Map<String, Object> response = realSoftwareClient.post(body);

        List<Map<String, Object>> comprobantes = (List<Map<String, Object>>) response.get("cmps");

        Map<String, Object> resultado = new HashMap<>();

        if (comprobantes == null || comprobantes.isEmpty()) {
            return resultado;
        }

        // primer comprobante, ultima fecha

        Map<String, Object> ultimaFactura = comprobantes.get(0);

        resultado.put("factura", ultimaFactura.get("numero"));
        resultado.put("fecha", ultimaFactura.get("fecha"));
        resultado.put("fechaVto", ultimaFactura.get("fecha_vto"));
        resultado.put("importe", ultimaFactura.get("importe"));
        resultado.put("saldo", ultimaFactura.get("saldo"));

        Number saldo = (Number) ultimaFactura.get("saldo");

        if (saldo != null && saldo.doubleValue() == 0) {
            resultado.put("estado", "Pagado");
        } else {
            resultado.put("estado", "Pendiente");
        }
        return resultado;
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
