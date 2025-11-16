package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class PagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "negocio_id", nullable = false)
    private NegocioEntity negocio;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    private String referencia;
    private String observaciones;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public PagoEntity() {
        this.fechaPago = LocalDateTime.now();
        this.fechaCreacion = LocalDateTime.now();
        this.monto = BigDecimal.ZERO;
        this.metodoPago = MetodoPago.EFECTIVO;
    }

    public enum MetodoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA, CHEQUE
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NegocioEntity getNegocio() { return negocio; }
    public void setNegocio(NegocioEntity negocio) { this.negocio = negocio; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}