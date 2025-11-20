package com.uptc.queenscorner.dtos;

import java.math.BigDecimal;

public class LineaFacturaResponse {
    
    private Long id;
    private Integer numeroLinea;
    private String descripcion;
    private BigDecimal cantidad;
    private BigDecimal valorUnitario;
    private BigDecimal total;
    
    public LineaFacturaResponse() {}
    
    public LineaFacturaResponse(Long id, Integer numeroLinea, String descripcion, 
                               BigDecimal cantidad, BigDecimal valorUnitario, BigDecimal total) {
        this.id = id;
        this.numeroLinea = numeroLinea;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.total = total;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getNumeroLinea() { return numeroLinea; }
    public void setNumeroLinea(Integer numeroLinea) { this.numeroLinea = numeroLinea; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
