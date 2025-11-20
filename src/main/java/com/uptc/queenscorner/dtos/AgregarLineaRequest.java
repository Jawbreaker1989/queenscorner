package com.uptc.queenscorner.dtos;

import java.math.BigDecimal;

public class AgregarLineaRequest {
    
    private String descripcion;
    private BigDecimal cantidad;
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
