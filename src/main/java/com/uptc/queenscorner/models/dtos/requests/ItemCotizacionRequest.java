package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;

/**
 * DTO que representa un item/línea dentro de una cotización.
 * 
 * Un item es un servicio o producto específico que se incluye en la cotización.
 * Puede ser:
 * - Un nuevo item (id = null)
 * - Un item existente a actualizar (id con valor)
 */
public class ItemCotizacionRequest {
    /** ID del item (null si es nuevo, valor si se actualiza uno existente) */
    private Long id;
    /** Descripción del servicio o producto a cotizar */
    private String descripcion;
    /** Cantidad de unidades */
    private Integer cantidad;
    /** Precio unitario del servicio/producto */
    private BigDecimal precioUnitario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
} 