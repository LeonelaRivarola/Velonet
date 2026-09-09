package com.velonet.backend.dto;

public class AltasComparativaDTO {

    private String periodo;
    private int cantidad;

    public AltasComparativaDTO(String periodo, int cantidad) {
        this.periodo = periodo;
        this.cantidad = cantidad;
    }

    public String getPeriodo() {
        return periodo;
    }

    public int getCantidad() {
        return cantidad;
    }
}