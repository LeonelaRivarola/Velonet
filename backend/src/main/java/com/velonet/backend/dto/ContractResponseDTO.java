package com.velonet.backend.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractResponseDTO {
    private String error;
    private List<ContractItemDTO> contratos;
    

    //Getters y Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public List<ContractItemDTO> getContratos() { return contratos; }    
    public void setContratos(List<ContractItemDTO> contratos) { this.contratos = contratos; }
}
