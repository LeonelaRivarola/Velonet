package com.velonet.backend.dto;

public class DashboardMetricsDTO {
    private long totalClientesActivos;
    private long totalContratoActivos;
    private long contratosSinCobroAuditoria;

    public DashboardMetricsDTO(long totalClientesActivos, long totalContratoActivos, long contratosSinCobroAuditoria) {
        this.totalClientesActivos = totalClientesActivos;
        this.totalContratoActivos = totalContratoActivos;
        this.contratosSinCobroAuditoria = contratosSinCobroAuditoria;
    }

    //Getters
    public long getTotalClientesActivos() { return totalClientesActivos; }
    public long getTotalContratoActivos() { return totalContratoActivos; }
    public long getContratosSinCobroAuditoria() { return contratosSinCobroAuditoria; }
}
