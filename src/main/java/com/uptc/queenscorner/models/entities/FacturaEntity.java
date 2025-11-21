package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas", indexes = {
    @Index(name = "idx_facturas_negocio_id", columnList = "negocio_id"),
    @Index(name = "idx_facturas_numero", columnList = "numero_factura"),
    @Index(name = "idx_facturas_estado", columnList = "estado"),
    @Index(name = "idx_facturas_fecha_creacion", columnList = "fecha_creacion")
})
public class FacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String numeroFactura;

    @Column(nullable = false, length = 255)
    private String codigo = "";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id", nullable = false)
    private NegocioEntity negocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id")
    private CotizacionEntity cotizacion;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineaFacturaEntity> lineas = new ArrayList<>();

    @Column(name = "subtotal_items", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotalItems = BigDecimal.ZERO;

    @Column(name = "anticipo", precision = 15, scale = 2, nullable = false)
    private BigDecimal anticipo = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal iva = BigDecimal.ZERO;

    @Column(name = "iva_19", precision = 15, scale = 2)
    private BigDecimal iva19;

    @Column(name = "total_a_pagar", precision = 15, scale = 2)
    private BigDecimal totalAPagar;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, length = 50)
    private String estado = "ENVIADA";

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "usuario_envio", length = 100)
    private String usuarioEnvio;

    @Column(name = "path_pdf", length = 500)
    private String pathPdf;

    public FacturaEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaEmision = LocalDateTime.now();
        this.codigo = "COD-" + System.currentTimeMillis();
        this.estado = "ENVIADA";
        this.subtotal = BigDecimal.ZERO;
        this.iva = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.anticipo = BigDecimal.ZERO;
        this.subtotalItems = BigDecimal.ZERO;
    }

    public void calcularTotales() {
        BigDecimal subtotalCalculado = lineas.stream()
            .map(LineaFacturaEntity::calcularTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.subtotal = subtotalCalculado;
        this.iva = subtotalCalculado.multiply(BigDecimal.valueOf(0.19));
        this.total = subtotalCalculado.add(this.iva);
    }

    public void generarNumeroFactura(long consecutivo) {
        int anio = this.fechaCreacion.getYear();
        this.numeroFactura = String.format("FAC-%d-%06d", anio, consecutivo);
    }

    public void cambiarAEnviada(String usuario) {
        this.usuarioEnvio = usuario;
        this.fechaEnvio = LocalDateTime.now();
    }

    public boolean puedeSerEnviada() {
        return !this.lineas.isEmpty() && this.negocio != null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public NegocioEntity getNegocio() { return negocio; }
    public void setNegocio(NegocioEntity negocio) { this.negocio = negocio; }

    public List<LineaFacturaEntity> getLineas() { return lineas; }
    public void setLineas(List<LineaFacturaEntity> lineas) { this.lineas = lineas; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    public String getUsuarioEnvio() { return usuarioEnvio; }
    public void setUsuarioEnvio(String usuarioEnvio) { this.usuarioEnvio = usuarioEnvio; }

    public String getPathPdf() { return pathPdf; }
    public void setPathPdf(String pathPdf) { this.pathPdf = pathPdf; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public CotizacionEntity getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }

    public BigDecimal getSubtotalItems() { return subtotalItems; }
    public void setSubtotalItems(BigDecimal subtotalItems) { this.subtotalItems = subtotalItems; }

    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }

    public BigDecimal getIva19() { return iva19; }
    public void setIva19(BigDecimal iva19) { this.iva19 = iva19; }

    public BigDecimal getTotalAPagar() { return totalAPagar; }
    public void setTotalAPagar(BigDecimal totalAPagar) { this.totalAPagar = totalAPagar; }
}
