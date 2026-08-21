package com.velonet.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_deudores")
public class HistorialDeudores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private Integer cantidad;

    public HistorialDeudores(){
    }

    public HistorialDeudores(LocalDate fecha, Integer cantidad){
        this.fecha = fecha;
        this.cantidad = cantidad;
    }
    
    public Long getId(){ return id; }
    public LocalDate getFecha(){ return fecha;  }
    public void setFecha(LocalDate fecha){ this.fecha = fecha; }
    public Integer getCantidad(){ return cantidad; }
    public void setCantidad(Integer cantidad){ this.cantidad = cantidad; }    

}
