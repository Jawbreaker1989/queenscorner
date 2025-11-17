package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "negocios")
public class NegocioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cotizacion_id", unique = true, nullable = false)
    private CotizacionEntity cotizacion;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoNegocio estado;

    @Column(name = "total_negocio")
    private BigDecimal totalNegocio;

    private BigDecimal anticipo;

    @Column(name = "saldo_pendiente")
    private BigDecimal saldoPendiente;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    private String descripcion;

    private String observaciones;

    public NegocioEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoNegocio.FINALIZADO;
        this.totalNegocio = BigDecimal.ZERO;
        this.anticipo = BigDecimal.ZERO;
        this.saldoPendiente = BigDecimal.ZERO;
    }

    public enum EstadoNegocio {
        CANCELADO, FINALIZADO
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CotizacionEntity getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public EstadoNegocio getEstado() { return estado; }
    public void setEstado(EstadoNegocio estado) { this.estado = estado; }
    public BigDecimal getTotalNegocio() { return totalNegocio; }
    public void setTotalNegocio(BigDecimal totalNegocio) { this.totalNegocio = totalNegocio; }
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}