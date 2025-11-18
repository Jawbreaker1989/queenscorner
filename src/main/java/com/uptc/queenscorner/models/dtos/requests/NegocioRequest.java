package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioRequest {
    private Long cotizacionId;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private BigDecimal presupuestoAsignado;
    private BigDecimal anticipo;
    private String responsable;
    private String observaciones;

    public Long getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(Long cotizacionId) { this.cotizacionId = cotizacionId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    public BigDecimal getPresupuestoAsignado() { return presupuestoAsignado; }
    public void setPresupuestoAsignado(BigDecimal presupuestoAsignado) { this.presupuestoAsignado = presupuestoAsignado; }
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}