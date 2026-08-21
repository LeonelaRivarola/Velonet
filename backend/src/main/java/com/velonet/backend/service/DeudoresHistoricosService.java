package com.velonet.backend.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.velonet.backend.dto.DeudoresComparativaDTO;
import com.velonet.backend.repository.HistorialDeudoresRepository;

@Service
public class DeudoresHistoricosService {
    private final HistorialDeudoresRepository repository;

    public DeudoresHistoricosService(HistorialDeudoresRepository repository){
        this.repository = repository;
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
