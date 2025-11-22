package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO que retorna los datos completos de un negocio.
 * 
 * Se utiliza cuando se consulta un negocio para mostrar:
 * - Información del cliente y la cotización original
 * - Detalles del proyecto (fechas, presupuesto, estado)
 * - Información denormalizada de la cotización para consultas rápidas
 * - Saldo pendiente y montos pagados
 * 
 * Nota: Los campos con prefijo "Cotizacion" son denormalizados
 * para evitar JOINs costosos en consultas frecuentes.
 */
public class NegocioResponse {

    /** ID único del negocio */
    private Long id;
    /** Código único del negocio (NEG-YYYYMMDD-XXXXX) */
    private String codigo;
    /** ID de la cotización original que originó este negocio */
    private Long cotizacionId;
    /** Información completa de la cotización (cuando se necesita completa) */
    private CotizacionResponse cotizacion; 
    /** Información del cliente (datos resumidos) */
    private ClienteResponse cliente;
    /** Fecha de creación del negocio */
    private LocalDateTime fechaCreacion;
    /** Fecha de última actualización */
    private LocalDateTime fechaActualizacion;

    /** CAMPOS DENORMALIZADOS DE LA COTIZACIÓN (para queries sin JOIN) */
    /** Código de la cotización original */
    private String codigoCotizacion;
    /** Estado de la cotización original */
    private String estadoCotizacion;
    /** Fecha de creación de la cotización */
    private LocalDateTime fechaCotizacion;
    /** Fecha de validez de la cotización */
    private LocalDate fechaValidezCotizacion;
    /** Descripción de la cotización */
    private String descripcionCotizacion;
    /** Subtotal de la cotización original */
    private BigDecimal subtotalCotizacion;
    /** Impuestos de la cotización original */
    private BigDecimal impuestosCotizacion;
    /** Total de la cotización original */
    private BigDecimal totalCotizacion;
    /** Observaciones de la cotización original */
    private String observacionesCotizacion;

    /** INFORMACIÓN DEL NEGOCIO/PROYECTO */
    /** Fecha de inicio del proyecto */
    private LocalDate fechaInicio;
    /** Fecha estimada de finalización */
    private LocalDate fechaFinEstimada;
    /** Estado del negocio (EN_REVISION, CANCELADO, FINALIZADO) */
    private String estado;
    /** Presupuesto total asignado al proyecto */
    private BigDecimal presupuestoAsignado;
    /** Anticipo pagado por el cliente */
    private BigDecimal anticipo;
    /** Saldo aún pendiente por cobrar */
    private BigDecimal saldoPendiente; 
    /** Responsable del proyecto */
    private String responsable;
    /** Descripción adicional del proyecto */
    private String descripcion;
    /** Observaciones sobre el proyecto */
    private String observaciones;

   
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public Long getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(Long cotizacionId) { this.cotizacionId = cotizacionId; }
    
    public CotizacionResponse getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionResponse cotizacion) { this.cotizacion = cotizacion; }
    
    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    
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
    
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}