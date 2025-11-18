package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NegocioResponse {
    // ===== DATOS DEL NEGOCIO =====
    private Long id;
    private String codigo;
    private Long cotizacionId;
    private ClienteResponse cliente;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // ===== DATOS DESNORMALIZADOS DE COTIZACIÓN (READ-ONLY) =====
    private String codigoCotizacion;
    private String estadoCotizacion;
    private LocalDateTime fechaCotizacion;
    private LocalDate fechaValidezCotizacion;
    private String descripcionCotizacion;
    private BigDecimal subtotalCotizacion;
    private BigDecimal impuestosCotizacion;
    private BigDecimal totalCotizacion;
    private String observacionesCotizacion;

    // ===== DATOS EDITABLES DEL NEGOCIO =====
    private LocalDate fechaInicio;
    private LocalDate fechaFinEstimada;
    private String estado;
    private BigDecimal presupuestoAsignado;
    private BigDecimal anticipo;
    private String responsable;
    private String descripcion;
    private String observaciones;

    // Getters y Setters - Datos Negocio Base
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public Long getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(Long cotizacionId) { this.cotizacionId = cotizacionId; }
    
    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    // Getters y Setters - Datos Desnormalizados Cotización
    public String getCodigoCotizacion() { return codigoCotizacion; }
    public void setCodigoCotizacion(String codigoCotizacion) { this.codigoCotizacion = codigoCotizacion; }
    
    public String getEstadoCotizacion() { return estadoCotizacion; }
    public void setEstadoCotizacion(String estadoCotizacion) { this.estadoCotizacion = estadoCotizacion; }
    
    public LocalDateTime getFechaCotizacion() { return fechaCotizacion; }
    public void setFechaCotizacion(LocalDateTime fechaCotizacion) { this.fechaCotizacion = fechaCotizacion; }
    
    public LocalDate getFechaValidezCotizacion() { return fechaValidezCotizacion; }
    public void setFechaValidezCotizacion(LocalDate fechaValidezCotizacion) { this.fechaValidezCotizacion = fechaValidezCotizacion; }
    
    public String getDescripcionCotizacion() { return descripcionCotizacion; }
    public void setDescripcionCotizacion(String descripcionCotizacion) { this.descripcionCotizacion = descripcionCotizacion; }
    
    public BigDecimal getSubtotalCotizacion() { return subtotalCotizacion; }
    public void setSubtotalCotizacion(BigDecimal subtotalCotizacion) { this.subtotalCotizacion = subtotalCotizacion; }
    
    public BigDecimal getImpuestosCotizacion() { return impuestosCotizacion; }
    public void setImpuestosCotizacion(BigDecimal impuestosCotizacion) { this.impuestosCotizacion = impuestosCotizacion; }
    
    public BigDecimal getTotalCotizacion() { return totalCotizacion; }
    public void setTotalCotizacion(BigDecimal totalCotizacion) { this.totalCotizacion = totalCotizacion; }
    
    public String getObservacionesCotizacion() { return observacionesCotizacion; }
    public void setObservacionesCotizacion(String observacionesCotizacion) { this.observacionesCotizacion = observacionesCotizacion; }

    // Getters y Setters - Datos Editables Negocio
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public BigDecimal getPresupuestoAsignado() { return presupuestoAsignado; }
    public void setPresupuestoAsignado(BigDecimal presupuestoAsignado) { this.presupuestoAsignado = presupuestoAsignado; }
    
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}