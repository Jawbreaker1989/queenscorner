package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "cotizaciones")
public class CotizacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_validez")
    private LocalDate fechaValidez;

    @Enumerated(EnumType.STRING)
    private EstadoCotizacion estado;

    private String descripcion;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private String observaciones;

    // Relación con items de cotización
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemCotizacionEntity> items;

    public CotizacionEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoCotizacion.BORRADOR;
        this.subtotal = BigDecimal.ZERO;
        this.impuestos = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.items = new ArrayList<>();
    }

    public enum EstadoCotizacion {
        BORRADOR, ENVIADA, APROBADA, RECHAZADA, CONVERTIDA
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public ClienteEntity getCliente() { return cliente; }
    public void setCliente(ClienteEntity cliente) { this.cliente = cliente; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDate getFechaValidez() { return fechaValidez; }
    public void setFechaValidez(LocalDate fechaValidez) { this.fechaValidez = fechaValidez; }
    public EstadoCotizacion getEstado() { return estado; }
    public void setEstado(EstadoCotizacion estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<ItemCotizacionEntity> getItems() { return items; }
    public void setItems(List<ItemCotizacionEntity> items) { this.items = items; }
}