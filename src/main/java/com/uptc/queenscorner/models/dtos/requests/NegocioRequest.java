package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO para crear o actualizar un negocio.
 * 
 * Se utiliza cuando una cotización ha sido aprobada y se necesita:
 * - Crear un negocio (proyecto) basado en la cotización
 * - Actualizar detalles del negocio (fechas, presupuesto, etc.)
 * 
 * Contiene información del proyecto como:
 * - Referencia a la cotización aprobada
 * - Fechas de inicio y fin estimadas
 * - Presupuesto asignado y anticipo
 * - Responsable del proyecto
 */
public class NegocioRequest {
    /** ID de la cotización aprobada que da origen al negocio */
    private Long cotizacionId;
    /** Descripción adicional del proyecto */
    private String descripcion;
    /** Fecha de inicio del proyecto (formato: YYYY-MM-DD) */
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate fechaInicio;
    /** Fecha estimada de finalización (formato: YYYY-MM-DD) */
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate fechaFinEstimada;
    /** Presupuesto total asignado al proyecto */
    private BigDecimal presupuestoAsignado;
    /** Monto de anticipo pagado por el cliente */
    private BigDecimal anticipo;
    /** Nombre de la persona responsable del proyecto */
    private String responsable;
    /** Observaciones sobre el proyecto */
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