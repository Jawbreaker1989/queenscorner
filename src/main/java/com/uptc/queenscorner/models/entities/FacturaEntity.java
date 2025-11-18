package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas", indexes = {
    @Index(name = "idx_negocio_id", columnList = "negocio_id"),
    @Index(name = "idx_cotizacion_id", columnList = "cotizacion_id"),
    @Index(name = "idx_numero_factura", columnList = "numero_factura"),
    @Index(name = "idx_estado", columnList = "estado")
})
public class FacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id", nullable = false)
    private NegocioEntity negocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private CotizacionEntity cotizacion;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineaFacturaEntity> lineas = new ArrayList<>();

    @Column(unique = true, nullable = false, length = 50)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal anticipo;

    @Column(name = "subtotal_items", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotalItems;

    @Column(insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal baseGravable;

    @Column(name = "iva_19", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal iva19;

    @Column(name = "total_a_pagar", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal totalAPagar;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_pago", nullable = false)
    private MedioPago medioPago;

    @Column(name = "referencia_pago", length = 255)
    private String referenciaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado = EstadoFactura.BORRADOR;

    @Column(name = "ruta_pdf", length = 500)
    private String rutaPdf;

    @Column(name = "pdf_generado")
    private Boolean pdfGenerado = false;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "condiciones_pago", length = 255)
    private String condicionesPago;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "usuario_emision", length = 100)
    private String usuarioEmision;

    @Column(name = "fecha_emision_real")
    private LocalDateTime fechaEmisionReal;

    @Column(name = "usuario_pago", length = 100)
    private String usuarioPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "fecha_ultima_modificacion")
    private LocalDateTime fechaUltimaModificacion;

    public enum MedioPago {
        TRANSFERENCIA("Transferencia Bancaria"),
        EFECTIVO("Efectivo"),
        CHEQUE("Cheque"),
        TARJETA("Tarjeta de Crédito"),
        OTRO("Otro medio de pago");

        private final String descripcion;

        MedioPago(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public enum EstadoFactura {
        BORRADOR,
        EMITIDA,
        ENVIADA,
        PAGADA,
        ANULADA
    }

    public FacturaEntity() {
        this.fechaEmision = LocalDateTime.now();
        this.estado = EstadoFactura.BORRADOR;
        this.pdfGenerado = false;
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaUltimaModificacion == null) {
            this.fechaUltimaModificacion = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaUltimaModificacion = LocalDateTime.now();
    }

    public void generarNumeroFactura(long consecutivo) {
        int anio = fechaEmision.getYear();
        this.numeroFactura = String.format("FAC-%d-%06d", anio, consecutivo);
    }

    public void cambiarEstado(EstadoFactura nuevoEstado, String usuario) {
        this.estado = nuevoEstado;
        if (nuevoEstado == EstadoFactura.EMITIDA && this.usuarioEmision == null) {
            this.usuarioEmision = usuario;
            this.fechaEmisionReal = LocalDateTime.now();
        }
        if (nuevoEstado == EstadoFactura.PAGADA) {
            this.usuarioPago = usuario;
            this.fechaPago = LocalDateTime.now();
        }
    }

    public boolean puedeEmitirse() {
        return this.estado == EstadoFactura.BORRADOR 
            && !lineas.isEmpty() 
            && this.negocio != null 
            && this.cotizacion != null;
    }

    public BigDecimal calcularBaseGravable() {
        return this.subtotalItems.subtract(this.anticipo);
    }

    public BigDecimal calcularIva() {
        return calcularBaseGravable().multiply(BigDecimal.valueOf(0.19));
    }

    public BigDecimal calcularTotal() {
        return calcularBaseGravable().add(calcularIva());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NegocioEntity getNegocio() { return negocio; }
    public void setNegocio(NegocioEntity negocio) { this.negocio = negocio; }

    public CotizacionEntity getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }

    public List<LineaFacturaEntity> getLineas() { return lineas; }
    public void setLineas(List<LineaFacturaEntity> lineas) { this.lineas = lineas; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }

    public BigDecimal getSubtotalItems() { return subtotalItems; }
    public void setSubtotalItems(BigDecimal subtotalItems) { this.subtotalItems = subtotalItems; }

    public BigDecimal getBaseGravable() { return baseGravable; }
    public BigDecimal getIva19() { return iva19; }
    public BigDecimal getTotalAPagar() { return totalAPagar; }

    public MedioPago getMedioPago() { return medioPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }

    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }

    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }

    public String getRutaPdf() { return rutaPdf; }
    public void setRutaPdf(String rutaPdf) { this.rutaPdf = rutaPdf; }

    public Boolean getPdfGenerado() { return pdfGenerado; }
    public void setPdfGenerado(Boolean pdfGenerado) { this.pdfGenerado = pdfGenerado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getCondicionesPago() { return condicionesPago; }
    public void setCondicionesPago(String condicionesPago) { this.condicionesPago = condicionesPago; }

    public String getUsuarioCreacion() { return usuarioCreacion; }
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    public String getUsuarioEmision() { return usuarioEmision; }
    public void setUsuarioEmision(String usuarioEmision) { this.usuarioEmision = usuarioEmision; }

    public LocalDateTime getFechaEmisionReal() { return fechaEmisionReal; }
    public void setFechaEmisionReal(LocalDateTime fechaEmisionReal) { this.fechaEmisionReal = fechaEmisionReal; }

    public String getUsuarioPago() { return usuarioPago; }
    public void setUsuarioPago(String usuarioPago) { this.usuarioPago = usuarioPago; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public LocalDateTime getFechaUltimaModificacion() { return fechaUltimaModificacion; }
    public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) { 
        this.fechaUltimaModificacion = fechaUltimaModificacion; 
    }
}
