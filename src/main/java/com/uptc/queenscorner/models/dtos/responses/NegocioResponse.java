package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NegocioResponse {
    private Long id;
    private String codigo;
    private CotizacionResponse cotizacion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private BigDecimal totalNegocio;
    private BigDecimal anticipo;
    private BigDecimal saldoPendiente;
    private LocalDate fechaEntregaEstimada;
    private String observaciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public CotizacionResponse getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionResponse cotizacion) { this.cotizacion = cotizacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getTotalNegocio() { return totalNegocio; }
    public void setTotalNegocio(BigDecimal totalNegocio) { this.totalNegocio = totalNegocio; }
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}