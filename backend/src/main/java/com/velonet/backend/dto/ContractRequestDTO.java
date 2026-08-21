package com.velonet.backend.dto;

public class ContractRequestDTO {
    private final String action = "contratos";
    private String incluye_bajas; //"S" o "N"
    private String estado_cliente; // "1", "2", etc

    //Getter y Setters
    public String getAction(){ return action; }
    public String getIncluyeBajas(){ return incluye_bajas; }
    public String getEstadoCliente(){ return estado_cliente; }
    public void setIncluyeBajas(String incluye_bajas){ this.incluye_bajas = incluye_bajas; }
    public void setEstadoCliente(String estado_cliente){ this.estado_cliente = estado_cliente; }
}
