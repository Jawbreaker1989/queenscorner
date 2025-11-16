package com.uptc.queenscorner.models.dtos.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrdenTrabajoResponse {
    private Long id;
    private String codigo;
    private NegocioResponse negocio;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaInicioEstimada;
    private LocalDate fechaFinEstimada;
    private LocalDateTime fechaEntregaReal;
    private String observaciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public NegocioResponse getNegocio() { return negocio; }
    public void setNegocio(NegocioResponse negocio) { this.negocio = negocio; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public LocalDate getFechaInicioEstimada() { return fechaInicioEstimada; }
    public void setFechaInicioEstimada(LocalDate fechaInicioEstimada) { this.fechaInicioEstimada = fechaInicioEstimada; }
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    public LocalDateTime getFechaEntregaReal() { return fechaEntregaReal; }
    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) { this.fechaEntregaReal = fechaEntregaReal; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}