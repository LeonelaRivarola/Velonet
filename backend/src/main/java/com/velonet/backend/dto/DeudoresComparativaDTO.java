package com.velonet.backend.dto;

public class DeudoresComparativaDTO {
    private String mes;
    private Integer cantidad;

    public DeudoresComparativaDTO(){}
    public DeudoresComparativaDTO(String mes, Integer cantidad){
        this.mes = mes;
        this.cantidad = cantidad;
    }

    public String getMes(){ return mes; }
    public void setMes(String mes){ this.mes = mes; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad){ this.cantidad = cantidad; }
}
