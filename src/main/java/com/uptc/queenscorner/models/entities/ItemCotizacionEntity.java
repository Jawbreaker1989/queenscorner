package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "items_cotizacion")
public class ItemCotizacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private CotizacionEntity cotizacion;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal precioUnitario;
    
    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public ItemCotizacionEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.cantidad = 1;
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CotizacionEntity getCotizacion() { return cotizacion; }
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}