package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que retorna los datos completos de una cotización.
 * 
 * Se utiliza cuando se consulta una cotización para mostrar:
 * - Información del cliente
 * - Detalles de la cotización (código, estado, fechas, etc.)
 * - Listado de items/servicios incluidos
 * - Cálculos finales (subtotal, impuestos, total)
 */
public class CotizacionResponse {
    /** ID único de la cotización */
    private Long id;
    /** Código único de la cotización (COT-YYYYMMDD-XXXXX) */
    private String codigo;
    /** Información del cliente (datos resumidos) */
    private ClienteResponse cliente;
    /** Fecha de creación de la cotización */
    private LocalDateTime fechaCreacion;
    /** Fecha hasta la cual es válida la cotización */
    private LocalDate fechaValidez;
    /** Estado actual (BORRADOR, ENVIADA, APROBADA, RECHAZADA) */
    private String estado;
    /** Descripción general de la propuesta */
    private String descripcion;
    /** Subtotal antes de impuestos */
    private BigDecimal subtotal;
    /** Total de impuestos (IVA 19%) */
    private BigDecimal impuestos;
    /** Total final (subtotal + impuestos) */
    private BigDecimal total;
    /** Observaciones adicionales */
    private String observaciones;
    /** Lista de items/servicios en la cotización */
    private List<ItemCotizacionResponse> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public ClienteResponse getCliente() { return cliente; }
    public void setCliente(ClienteResponse cliente) { this.cliente = cliente; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDate getFechaValidez() { return fechaValidez; }
    public void setFechaValidez(LocalDate fechaValidez) { this.fechaValidez = fechaValidez; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<ItemCotizacionResponse> getItems() { return items; }
    public void setItems(List<ItemCotizacionResponse> items) { this.items = items; }
} 