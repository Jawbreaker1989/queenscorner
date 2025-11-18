package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "negocios")
public class NegocioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cotizacion_id", unique = true, nullable = false)
    private CotizacionEntity cotizacion;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ===== DATOS DESNORMALIZADOS DE COTIZACIÓN (READ-ONLY) =====
    @Column(name = "codigo_cotizacion")
    private String codigoCotizacion;

    @Column(name = "estado_cotizacion")
    private String estadoCotizacion;

    @Column(name = "fecha_cotizacion")
    private LocalDateTime fechaCotizacion;

    @Column(name = "fecha_validez_cotizacion")
    private LocalDate fechaValidezCotizacion;

    @Column(name = "descripcion_cotizacion")
    private String descripcionCotizacion;

    @Column(name = "subtotal_cotizacion", precision = 15, scale = 2)
    private BigDecimal subtotalCotizacion;

    @Column(name = "impuestos_cotizacion", precision = 15, scale = 2)
    private BigDecimal impuestosCotizacion;

    @Column(name = "total_cotizacion", precision = 15, scale = 2)
    private BigDecimal totalCotizacion;

    @Column(name = "observaciones_cotizacion")
    private String observacionesCotizacion;

    // ===== DATOS DEL NEGOCIO (EDITABLES) =====
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Enumerated(EnumType.STRING)
    private EstadoNegocio estado;

    @Column(name = "presupuesto_asignado", precision = 15, scale = 2)
    private BigDecimal presupuestoAsignado;

    @Column(name = "presupuesto_utilizado", precision = 15, scale = 2)
    private BigDecimal presupuestoUtilizado;

    @Column(name = "responsable")
    private String responsable;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public NegocioEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoNegocio.EN_REVISION;
        this.presupuestoAsignado = BigDecimal.ZERO;
        this.presupuestoUtilizado = BigDecimal.ZERO;
    }

    public enum EstadoNegocio {
        EN_REVISION, CANCELADO, FINALIZADO
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public CotizacionEntity getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // DESNORMALIZADOS DE COTIZACIÓN
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

    // DATOS DEL NEGOCIO
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    
    public EstadoNegocio getEstado() { return estado; }
    public void setEstado(EstadoNegocio estado) { this.estado = estado; }
    
    public BigDecimal getPresupuestoAsignado() { return presupuestoAsignado; }
    public void setPresupuestoAsignado(BigDecimal presupuestoAsignado) { this.presupuestoAsignado = presupuestoAsignado; }
    
    public BigDecimal getPresupuestoUtilizado() { return presupuestoUtilizado; }
    public void setPresupuestoUtilizado(BigDecimal presupuestoUtilizado) { this.presupuestoUtilizado = presupuestoUtilizado; }
    
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}