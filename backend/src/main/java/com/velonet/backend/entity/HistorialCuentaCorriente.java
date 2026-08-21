package com.velonet.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_cuenta_corriente")
public class HistorialCuentaCorriente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private double monto;

    public HistorialCuentaCorriente() {}

    public HistorialCuentaCorriente(LocalDate fecha, double monto) {
        this.fecha = fecha;
        this.monto = monto;
    }

    public Long getId(){ return id; }
    public LocalDate getfecha(){ return fecha; }
    public double getMonto(){ return monto; }   
    public void setfecha(LocalDate fecha){ this.fecha = fecha; }
    public void setMonto(double monto){ this.monto = monto; }

}
