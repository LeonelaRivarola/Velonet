package com.velonet.backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.velonet.backend.dto.DeudoresComparativaDTO;
import com.velonet.backend.entity.HistorialDeudores;
import com.velonet.backend.repository.HistorialDeudoresRepository;

@Service
public class DeudoresHistoricosService {
    private final HistorialDeudoresRepository repository;
    private final DeudoresService deudoresService; 

    public DeudoresHistoricosService(HistorialDeudoresRepository repository, DeudoresService deudoresService) {
        this.repository = repository;
        this.deudoresService = deudoresService;
    }

    @Async
    public void guardarDeudoresHoy(){
        LocalDate hoy = LocalDate.now();

        if(repository.existsByFecha(hoy)){
            System.out.println("El snapshot de deudores para  " + hoy + "ya fue procesado");
            return;
        }

        try{
            int cantidad = deudoresService.getCantidadDeudoresActual();
            HistorialDeudores registro = new HistorialDeudores(hoy,cantidad);
            repository.save(registro);
            System.out.println("Snapshot de deudores guardadao: " + cantidad);
        }catch (Exception e){
            System.out.println("Error al guardar snapshot de deudores: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 5 3 * * *") // Todos los días a las 3:05 AM
    public void guardarDeudoresHoyProgramado() {
        guardarDeudoresHoy();
    }
    public List<DeudoresComparativaDTO> obtenerHistorico(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

        return repository.findAllByOrderByFechaAsc()
                    .stream()
                    .map(h -> new DeudoresComparativaDTO(
                                    h.getFecha().format(formatter),
                                    h.getCantidad()))
                    .collect(Collectors.toList());
    }
}
