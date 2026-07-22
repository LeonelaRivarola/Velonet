package com.velonet.backend.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientResponseDTO {
    private int error;
    private String status;
    private int resultados;
    //Map para capturar IDs dinamicos como "2": {...}
    private Map<String, ClientItemDTO> clientes;

    //Getters y Setters
    public int getError() { return error; }
    public void setError(int error) { this.error = error; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getResultados() { return resultados; }
    public void setResultados(int resultados) { this.resultados = resultados; }
    public Map<String, ClientItemDTO> getClientes() { return clientes; }
    public void setClientes(Map<String, ClientItemDTO> clientes) { this.clientes = clientes; }
}
