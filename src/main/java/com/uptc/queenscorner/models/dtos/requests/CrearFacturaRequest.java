package com.uptc.queenscorner.models.dtos.requests;

import java.util.List;

/**
 * DTO para crear una factura.
 * 
 * Se utiliza cuando se desea generar una factura basada en un negocio completado.
 * 
 * Contiene:
 * - Referencia al negocio
 * - Referencia a la cotización original
 * - Lista de líneas de detalle de la factura
 */
public class CrearFacturaRequest {
    
    /** ID del negocio al que se le facturará */
    private Long negocioId;
    /** ID de la cotización original (opcional, para referencia) */
    private Long cotizacionId;
    /** Líneas de detalle de la factura */
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
