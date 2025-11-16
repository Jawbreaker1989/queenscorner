package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CotizacionResponse {
    private Long id;
    private String codigo;
    private ClienteResponse cliente;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaValidez;
    private String estado;
    private String descripcion;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private String observaciones;
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