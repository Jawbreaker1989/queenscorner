package com.uptc.queenscorner.models.dtos.requests;

import java.time.LocalDate;

public class OrdenTrabajoRequest {
    private Long negocioId;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaInicioEstimada;
    private LocalDate fechaFinEstimada;
    private String observaciones;

    public Long getNegocioId() { return negocioId; }
    public void setNegocioId(Long negocioId) { this.negocioId = negocioId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public LocalDate getFechaInicioEstimada() { return fechaInicioEstimada; }
    public void setFechaInicioEstimada(LocalDate fechaInicioEstimada) { this.fechaInicioEstimada = fechaInicioEstimada; }
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}