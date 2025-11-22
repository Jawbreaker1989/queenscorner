package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Negocio (Proyecto)
 * Representa los proyectos/negocios creados a partir de cotizaciones aprobadas
 * Un negocio es la evolución de una cotización hacia su ejecución
 * 
 * Tabla: negocios
 * Estados: EN_REVISION → FINALIZADO o CANCELADO
 * 
 * Características:
 * - Código único generado automáticamente: NEG-YYYYMMDD-XXXXX
 * - Relación 1:1 con Cotización (única)
 * - Datos desnormalizados de cotización (para consultas rápidas)
 * - Seguimiento de presupuesto y anticipo
 * - Información de ejecución del proyecto
 */
@Entity
@Table(name = "negocios")
public class NegocioEntity {
    
    /** Identificador único en la BD */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cotización aprobada que originó este negocio (1:1) */
    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "cotizacion_id", unique = true, nullable = false)
    private CotizacionEntity cotizacion;

    /** Código único del negocio: NEG-YYYYMMDD-XXXXX */
    @Column(unique = true, nullable = false)
    private String codigo;

    /** Fecha de creación del negocio */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // ===== DATOS DESNORMALIZADOS DE COTIZACIÓN (READ-ONLY) =====
    // Copia de datos de cotización para consultas rápidas sin JOIN
    
    /** Código de la cotización (copia) */
    @Column(name = "codigo_cotizacion")
    private String codigoCotizacion;

    /** Estado de la cotización (copia) */
    @Column(name = "estado_cotizacion")
    private String estadoCotizacion;

    /** Fecha de creación de la cotización (copia) */
    @Column(name = "fecha_cotizacion")
    private LocalDateTime fechaCotizacion;

    /** Fecha de vigencia de la cotización (copia) */
    @Column(name = "fecha_validez_cotizacion")
    private LocalDate fechaValidezCotizacion;

    /** Descripción de la cotización (copia) */
    @Column(name = "descripcion_cotizacion")
    private String descripcionCotizacion;

    /** Subtotal de la cotización (copia) */
    @Column(name = "subtotal_cotizacion", precision = 15, scale = 2)
    private BigDecimal subtotalCotizacion;

    /** Impuestos de la cotización (copia) */
    @Column(name = "impuestos_cotizacion", precision = 15, scale = 2)
    private BigDecimal impuestosCotizacion;

    /** Total de la cotización (copia) */
    @Column(name = "total_cotizacion", precision = 15, scale = 2)
    private BigDecimal totalCotizacion;

    /** Observaciones de la cotización (copia) */
    @Column(name = "observaciones_cotizacion")
    private String observacionesCotizacion;

    // ===== DATOS DEL NEGOCIO =====
    
    /** Fecha prevista de inicio del proyecto */
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    /** Fecha estimada de finalización */
    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    /** Estado actual: EN_REVISION, CANCELADO, FINALIZADO */
    @Enumerated(EnumType.STRING)
    private EstadoNegocio estado;

    /** Presupuesto total asignado para el proyecto */
    @Column(name = "presupuesto_asignado", precision = 15, scale = 2)
    private BigDecimal presupuestoAsignado;

    /** Anticipo pagado por el cliente */
    @Column(name = "anticipo", precision = 15, scale = 2)
    private BigDecimal anticipo;

    /** Persona responsable del proyecto */
    @Column(name = "responsable")
    private String responsable;

    /** Descripción del negocio/proyecto */
    @Column(name = "descripcion")
    private String descripcion;

    /** Observaciones o notas adicionales */
    @Column(name = "observaciones")
    private String observaciones;

    /** Fecha de última actualización */
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Enum para los estados del negocio
     * EN_REVISION: En ejecución o evaluación
     * CANCELADO: Proyecto cancelado
     * FINALIZADO: Proyecto completado
     */
    public enum EstadoNegocio {
        EN_REVISION, CANCELADO, FINALIZADO
    }

    /**
     * Constructor por defecto
     * Inicializa valores por defecto
     */
    public NegocioEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoNegocio.EN_REVISION;
        this.presupuestoAsignado = BigDecimal.ZERO;
        this.anticipo = BigDecimal.ZERO;
    }

    // ===== GETTERS Y SETTERS PRINCIPALES =====
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public CotizacionEntity getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // ===== GETTERS Y SETTERS DE DATOS DESNORMALIZADOS =====
    
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

    // ===== GETTERS Y SETTERS DEL NEGOCIO =====
    
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    
    public EstadoNegocio getEstado() { return estado; }
    public void setEstado(EstadoNegocio estado) { this.estado = estado; }
    
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

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}