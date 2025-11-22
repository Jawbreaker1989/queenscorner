package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO que retorna los datos completos de una factura.
 * 
 * Se utiliza cuando se consulta una factura para mostrar:
 * - Número y estado de la factura
 * - Información del negocio asociado
 * - Listado de líneas de detalle
 * - Cálculos finales (subtotal, IVA, total, saldo pendiente)
 * - Información de auditoría (usuario, fechas)
 */
public class FacturaResponse {
    
    /** ID único de la factura */
    private Long id;
    /** Número único de factura (FAC-AAAA-CCCCCC) */
    private String numeroFactura;
    /** Fecha de creación del registro en BD */
    private LocalDateTime fechaCreacion;
    /** Fecha en que fue enviada al cliente */
    private LocalDateTime fechaEnvio;
    /** Estado de la factura (ENVIADA, EN_REVISION, PAGADA, etc.) */
    private String estado;
    /** Subtotal de la factura (sin IVA) */
    private BigDecimal subtotal;
    /** IVA calculado (19% del subtotal) */
    private BigDecimal iva;
    /** Total final (subtotal + IVA) */
    private BigDecimal total;
    /** Anticipo pagado por el cliente */
    private BigDecimal anticipo;
    /** Saldo pendiente por cobrar (total - anticipo) */
    private BigDecimal saldoPendiente;
    /** Observaciones sobre la factura */
    private String observaciones;
    /** Usuario que creó la factura */
    private String usuarioCreacion;
    /** Usuario que envió la factura */
    private String usuarioEnvio;
    /** Ruta del PDF generado */
    private String pathPdf;
    /** Información del negocio al que pertenece la factura */
    private NegocioInfoResponse negocio;
    /** Lista de líneas de detalle de la factura */
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
    
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    
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
