package com.uptc.queenscorner.dtos;

import java.util.List;

public class CrearFacturaRequest {
    
    private Long negocioId;
    private Long cotizacionId;
    private List<AgregarLineaRequest> lineas;
    
    public CrearFacturaRequest() {}
    
    public CrearFacturaRequest(Long negocioId, Long cotizacionId) {
        this.negocioId = negocioId;
        this.cotizacionId = cotizacionId;
    }
    
    public Long getNegocioId() { return negocioId; }
    public void setNegocioId(Long negocioId) { this.negocioId = negocioId; }
    
    public Long getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(Long cotizacionId) { this.cotizacionId = cotizacionId; }
    
    public List<AgregarLineaRequest> getLineas() { return lineas; }
    public void setLineas(List<AgregarLineaRequest> lineas) { this.lineas = lineas; }
} 
