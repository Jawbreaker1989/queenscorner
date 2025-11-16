package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FacturaResponse {
    private Long id;
    private String codigo;
    private NegocioResponse negocio;
    private LocalDateTime fechaEmision;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private String estado;
    private String pdfPath;
    private LocalDateTime fechaEnvio;
    private String observaciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public NegocioResponse getNegocio() { return negocio; }
    public void setNegocio(NegocioResponse negocio) { this.negocio = negocio; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}