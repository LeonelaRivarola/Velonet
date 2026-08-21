package com.velonet.backend.dto;

//DTO para enviar al front para la curva
public class CurvaPagoMensualDTO {
    private String mesAnio; // Formato: "yyyy-mm"
    private Double totalCobrado;

    public CurvaPagoMensualDTO(String mesAnio, Double totalCobrado) {
        this.mesAnio = mesAnio;
        this.totalCobrado = totalCobrado;
    }

    //Getters y Setters
    public String getMesAnio() { return mesAnio; }      
    public void setMesAnio(String mesAnio) { this.mesAnio = mesAnio; }
    public Double getTotalCobrado() { return totalCobrado; }
    public void setTotalCobrado(Double totalCobrado) { this.totalCobrado = totalCobrado; }
}
