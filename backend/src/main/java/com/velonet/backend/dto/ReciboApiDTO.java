package com.velonet.backend.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

//mapear la respuesta de la api de gestion
public class ReciboApiDTO {
    
    @JsonProperty("fecha_tipo")  //ver nombre de campo
    private String fecha;

    @JsonProperty("importe")
    private BigDecimal importe;

    //Getter y Seters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
}
