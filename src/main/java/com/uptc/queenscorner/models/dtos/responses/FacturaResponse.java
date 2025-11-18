package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FacturaResponse {
    
    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private LocalDate fechaVencimiento;
    
    private String nombreNegocio;
    private String nombreCliente;
    private String rutCliente;
    private String emailCliente;
    private String telefonoCliente;
    
    private BigDecimal anticipo;
    private BigDecimal subtotalItems;
    private BigDecimal baseGravable;
    private BigDecimal iva19;
    private BigDecimal totalAPagar;
    
    private String medioPago;
    private String referenciaPago;
    
    private String estado;
    private Boolean pdfGenerado;
    private String rutaPdf;
    
    private String notas;
    private String condicionesPago;
    
    private List<LineaResponse> lineas;

    public static class LineaResponse {
        private Long id;
        private Integer numeroLinea;
        private String descripcion;
        private Integer cantidad;
        private BigDecimal valorUnitario;
        private BigDecimal valorLinea;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Integer getNumeroLinea() { return numeroLinea; }
        public void setNumeroLinea(Integer numeroLinea) { this.numeroLinea = numeroLinea; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
        public BigDecimal getValorLinea() { return valorLinea; }
        public void setValorLinea(BigDecimal valorLinea) { this.valorLinea = valorLinea; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getNombreNegocio() { return nombreNegocio; }
    public void setNombreNegocio(String nombreNegocio) { this.nombreNegocio = nombreNegocio; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getRutCliente() { return rutCliente; }
    public void setRutCliente(String rutCliente) { this.rutCliente = rutCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    public BigDecimal getSubtotalItems() { return subtotalItems; }
    public void setSubtotalItems(BigDecimal subtotalItems) { this.subtotalItems = subtotalItems; }
    public BigDecimal getBaseGravable() { return baseGravable; }
    public void setBaseGravable(BigDecimal baseGravable) { this.baseGravable = baseGravable; }
    public BigDecimal getIva19() { return iva19; }
    public void setIva19(BigDecimal iva19) { this.iva19 = iva19; }
    public BigDecimal getTotalAPagar() { return totalAPagar; }
    public void setTotalAPagar(BigDecimal totalAPagar) { this.totalAPagar = totalAPagar; }
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Boolean getPdfGenerado() { return pdfGenerado; }
    public void setPdfGenerado(Boolean pdfGenerado) { this.pdfGenerado = pdfGenerado; }
    public String getRutaPdf() { return rutaPdf; }
    public void setRutaPdf(String rutaPdf) { this.rutaPdf = rutaPdf; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public String getCondicionesPago() { return condicionesPago; }
    public void setCondicionesPago(String condicionesPago) { this.condicionesPago = condicionesPago; }
    public List<LineaResponse> getLineas() { return lineas; }
    public void setLineas(List<LineaResponse> lineas) { this.lineas = lineas; }
}
