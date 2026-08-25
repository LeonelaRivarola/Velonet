package com.velonet.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.velonet.backend.entity.HistorialCuentaCorriente;

public interface HistorialCuentaCorrienteRepository extends JpaRepository<HistorialCuentaCorriente, Long> {
    boolean existsByFecha(LocalDate hoy);
    List<HistorialCuentaCorriente> findTop10ByOrderByFechaDesc();
}
