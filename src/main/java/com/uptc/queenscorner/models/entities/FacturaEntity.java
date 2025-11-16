package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
public class FacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "negocio_id", unique = true, nullable = false)
    private NegocioEntity negocio;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    @Column(name = "pdf_path")
    private String pdfPath;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    private String observaciones;

    public FacturaEntity() {
        this.fechaEmision = LocalDateTime.now();
        this.estado = EstadoFactura.GENERADA;
        this.subtotal = BigDecimal.ZERO;
        this.impuestos = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
    }

    public enum EstadoFactura {
        GENERADA, ENVIADA, PAGADA, CANCELADA
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NegocioEntity getNegocio() { return negocio; }
    public void setNegocio(NegocioEntity negocio) { this.negocio = negocio; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}