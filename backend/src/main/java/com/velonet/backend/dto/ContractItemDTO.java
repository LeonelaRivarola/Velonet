package com.velonet.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractItemDTO {
    private String id;
    private String estado;
    private String cliente_id;
    private String nombre;
    private String observaciones;

    //Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getClienteId() { return cliente_id; }
    public void setClienteId(String cliente_id) { this.cliente_id = cliente_id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

}
