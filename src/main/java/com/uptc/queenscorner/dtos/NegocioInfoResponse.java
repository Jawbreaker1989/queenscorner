package com.uptc.queenscorner.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NegocioInfoResponse {
    
    private Long id;
    private String numero;
    private LocalDateTime fechaCreacion;
    private String proyecto;
    private BigDecimal totalCotizacion;
    private BigDecimal anticipo;
    private BigDecimal saldoPendiente;
    private ClienteInfoResponse cliente;
    
    public NegocioInfoResponse() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getProyecto() { return proyecto; }
    public void setProyecto(String proyecto) { this.proyecto = proyecto; }
    
    public BigDecimal getTotalCotizacion() { return totalCotizacion; }
    public void setTotalCotizacion(BigDecimal totalCotizacion) { this.totalCotizacion = totalCotizacion; }
    
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    
    public ClienteInfoResponse getCliente() { return cliente; }
    public void setCliente(ClienteInfoResponse cliente) { this.cliente = cliente; }
}
