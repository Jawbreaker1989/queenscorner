package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoResponse {
    private Long id;
    private NegocioResponse negocio;
    private LocalDateTime fechaPago;
    private BigDecimal monto;
    private String metodoPago;
    private String referencia;
    private String observaciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NegocioResponse getNegocio() { return negocio; }
    public void setNegocio(NegocioResponse negocio) { this.negocio = negocio; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}