package com.velonet.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientItemDTO {
    private String nombre;
    private ClientStatusDTO estado;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public ClientStatusDTO getEstado() { return estado; }
    public void setEstado(ClientStatusDTO estado) { this.estado = estado; }
}
