package com.velonet.backend.dto;


//mapear la respuesta de la api de gestion
public class ReciboDTO {
    
    private String fecha;
    private Double importe;

    public ReciboDTO(String fecha, Double importe){
        this.fecha = fecha;
        this.importe = importe;
    }

    //Getter y Seters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }
}
