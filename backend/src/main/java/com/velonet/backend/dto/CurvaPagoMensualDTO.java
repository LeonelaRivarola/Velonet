package com.velonet.backend.dto;

import java.math.BigDecimal;

//DTO para enviar al front para la curva
public class CurvaPagoMensualDTO {
    private String mesAnio; // Formato: "yyyy-mm"
    private BigDecimal totalCobrado;

    public CurvaPagoMensualDTO(String mesAnio, BigDecimal totalCobrado) {
        this.mesAnio = mesAnio;
        this.totalCobrado = totalCobrado;
    }

    //Getters y Setters
    public String getMesAnio() { return mesAnio; }      
    public void setMesAnio(String mesAnio) { this.mesAnio = mesAnio; }
    public BigDecimal getTotalCobrado() { return totalCobrado; }
    public void setTotalCobrado(BigDecimal totalCobrado) { this.totalCobrado = totalCobrado; }
}
