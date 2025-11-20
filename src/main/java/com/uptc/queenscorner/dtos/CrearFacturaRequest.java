package com.uptc.queenscorner.dtos;

import java.util.List;

public class CrearFacturaRequest {
    
    private Long negocioId;
    private String observaciones;
    private List<AgregarLineaRequest> lineas;
    
    public CrearFacturaRequest() {}
    
    public CrearFacturaRequest(Long negocioId, String observaciones) {
        this.negocioId = negocioId;
        this.observaciones = observaciones;
    }
    
    public Long getNegocioId() { return negocioId; }
    public void setNegocioId(Long negocioId) { this.negocioId = negocioId; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public List<AgregarLineaRequest> getLineas() { return lineas; }
    public void setLineas(List<AgregarLineaRequest> lineas) { this.lineas = lineas; }
}
