package com.velonet.backend.repository;

import java.time.LocalDate;
import java.util.List;

import com.velonet.backend.entity.HistorialDeudores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialDeudoresRepository extends JpaRepository<HistorialDeudores, Long>{
    
    boolean existsByFecha(LocalDate fecha);

    List<HistorialDeudores> findAllByOrderByFechaAsc();
}
