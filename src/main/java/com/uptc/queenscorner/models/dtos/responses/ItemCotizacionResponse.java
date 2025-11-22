package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;

/**
 * DTO que retorna los datos de un item de cotización.
 * 
 * Se utiliza cuando se devuelven los detalles de una cotización,
 * incluyendo el listado de items/servicios que la componen.
 * 
 * Contiene el ID, descripción, cantidad, precio unitario y subtotal calculado.
 */
public class ItemCotizacionResponse {
    /** ID único del item */
    private Long id;
    /** Descripción del servicio/producto */
    private String descripcion;
    /** Cantidad de unidades */
    private Integer cantidad;
    /** Precio unitario */
    private BigDecimal precioUnitario;
    /** Subtotal calculado (cantidad * precioUnitario) */
    private BigDecimal subtotal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}  