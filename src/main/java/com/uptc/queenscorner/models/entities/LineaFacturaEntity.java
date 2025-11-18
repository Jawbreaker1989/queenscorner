package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lineas_factura", indexes = {
    @Index(name = "idx_factura_id", columnList = "factura_id"),
    @Index(name = "idx_item_cotizacion_id", columnList = "item_cotizacion_id")
})
public class LineaFacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private FacturaEntity factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_cotizacion_id")
    private ItemCotizacionEntity itemCotizacion;

    @Column(name = "numero_linea", nullable = false)
    private Integer numeroLinea;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad = 1;

    @Column(name = "valor_unitario", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "valor_linea", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal valorLinea;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public LineaFacturaEntity() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FacturaEntity getFactura() { return factura; }
    public void setFactura(FacturaEntity factura) { this.factura = factura; }

    public ItemCotizacionEntity getItemCotizacion() { return itemCotizacion; }
    public void setItemCotizacion(ItemCotizacionEntity itemCotizacion) { this.itemCotizacion = itemCotizacion; }

    public Integer getNumeroLinea() { return numeroLinea; }
    public void setNumeroLinea(Integer numeroLinea) { this.numeroLinea = numeroLinea; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public BigDecimal getValorLinea() { return valorLinea; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
