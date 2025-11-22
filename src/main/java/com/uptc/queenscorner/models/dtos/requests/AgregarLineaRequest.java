package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;

/**
 * DTO que representa una línea de detalle para agregar a una factura.
 * 
 * Cada línea contiene un servicio o producto específico con su:
 * - Descripción
 * - Cantidad
 * - Valor unitario
 * 
 * El total de la línea se calcula: cantidad * valor unitario
 */
public class AgregarLineaRequest {
    
    /** Descripción del servicio o producto facturado */
    private String descripcion;
    /** Cantidad de unidades */
    private BigDecimal cantidad;
    /** Precio unitario del servicio/producto */
    private BigDecimal valorUnitario;
    
    public AgregarLineaRequest() {}
    
    public AgregarLineaRequest(String descripcion, BigDecimal cantidad, BigDecimal valorUnitario) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
}  
 