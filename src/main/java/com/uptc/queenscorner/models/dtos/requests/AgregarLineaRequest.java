package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;

public class AgregarLineaRequest {
    private String descripcion;
    private Integer cantidad;
    private BigDecimal valorUnitario;

    public AgregarLineaRequest() {
    }

    public AgregarLineaRequest(String descripcion, Integer cantidad, BigDecimal valorUnitario) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
}
