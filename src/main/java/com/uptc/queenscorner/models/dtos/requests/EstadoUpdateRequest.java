package com.uptc.queenscorner.models.dtos.requests;

/**
 * DTO para actualizar el estado de una cotización o negocio.
 * 
 * Se utiliza cuando se necesita cambiar el estado de un documento:
 * - Cotización: BORRADOR → ENVIADA → APROBADA/RECHAZADA
 * - Negocio: EN_REVISION → CANCELADO/FINALIZADO
 * 
 * También permite agregar observaciones al cambio de estado.
 */
public class EstadoUpdateRequest {
    /** Nuevo estado a establecer */
    private String estado;
    /** Observaciones o notas sobre el cambio de estado */
    private String observaciones;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
} 