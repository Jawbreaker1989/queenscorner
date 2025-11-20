package com.uptc.queenscorner.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FacturaResponse {
    
    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
    private String estado;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private String observaciones;
    private String usuarioCreacion;
    private String usuarioEnvio;
    private String pathPdf;
    private NegocioInfoResponse negocio;
    private List<LineaFacturaResponse> lineas = new ArrayList<>();
    
    public FacturaResponse() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public String getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }
    
    public String getUsuarioEnvio() { return usuarioEnvio; }
    public void setUsuarioEnvio(String usuarioEnvio) { this.usuarioEnvio = usuarioEnvio; }
    
    public String getPathPdf() { return pathPdf; }
    public void setPathPdf(String pathPdf) { this.pathPdf = pathPdf; }
    
    public NegocioInfoResponse getNegocio() { return negocio; }
    public void setNegocio(NegocioInfoResponse negocio) { this.negocio = negocio; }
    
    public List<LineaFacturaResponse> getLineas() { return lineas; }
    public void setLineas(List<LineaFacturaResponse> lineas) { this.lineas = lineas; }
}
