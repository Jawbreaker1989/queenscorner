package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioRequest {
    private Long cotizacionId;
    private String descripcion;
    private BigDecimal anticipo;
    private LocalDate fechaEntregaEstimada;
    private String observaciones;

    public Long getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(Long cotizacionId) { this.cotizacionId = cotizacionId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}