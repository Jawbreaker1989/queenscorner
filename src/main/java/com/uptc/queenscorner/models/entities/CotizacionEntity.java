package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad Cotización (Presupuesto)
 * Representa cotizaciones/presupuestos ofrecidos a clientes
 * Es la base para crear negocios cuando se aprueba
 * 
 * Tabla: cotizaciones
 * Estados: BORRADOR → ENVIADA → APROBADA o RECHAZADA
 * 
 * Características:
 * - Código único generado automáticamente
 * - Número secuencial único
 * - Vigencia configurable
 * - Cálculo automático de totales e IVA
 * - Relación con items (líneas de presupuesto)
 */
@Entity
@Table(name = "cotizaciones")
public class CotizacionEntity {
    
    /** Identificador único en la BD */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código único: COT-YYYYMMDD-XXXXX */
    @Column(unique = true, nullable = false)
    private String codigo;

    /** Número secuencial único de cotización */
    @Column(name = "numero_cotizacion", unique = true, nullable = false)
    private String numeroCotizacion;

    /** Cliente a quien va dirigida la cotización */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    /** Fecha de creación de la cotización */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /** Fecha hasta la cual es válida la cotización */
    @Column(name = "fecha_validez")
    private LocalDate fechaValidez;

    /** Estado actual: BORRADOR, ENVIADA, APROBADA, RECHAZADA */
    @Enumerated(EnumType.STRING)
    private EstadoCotizacion estado;

    /** Descripción general del presupuesto */
    private String descripcion;
    
    /** Subtotal sin impuestos */
    private BigDecimal subtotal;
    
    /** Impuestos (IVA 19%) */
    private BigDecimal impuestos;
    
    /** Total final (subtotal + impuestos) */
    private BigDecimal total;
    
    /** Observaciones o notas adicionales */
    private String observaciones;

    /** Items/líneas que componen la cotización */
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ItemCotizacionEntity> items;

    /**
     * Enum para los estados de la cotización
     * BORRADOR: En edición, no enviada
     * ENVIADA: Enviada al cliente, pendiente respuesta
     * APROBADA: Cliente aprobó, puede generar negocio
     * RECHAZADA: Cliente rechazó
     */
    public enum EstadoCotizacion {
        BORRADOR, ENVIADA, APROBADA, RECHAZADA
    }

    /**
     * Constructor por defecto
     * Inicializa valores por defecto
     */
    public CotizacionEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoCotizacion.BORRADOR;
        this.subtotal = BigDecimal.ZERO;
        this.impuestos = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.items = new ArrayList<>();
    }

    // ===== GETTERS Y SETTERS =====
    
    /** @return ID único */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    /** @return Código único COT-YYYYMMDD-XXXXX */
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    /** @return Número secuencial */
    public String getNumeroCotizacion() { return numeroCotizacion; }
    public void setNumeroCotizacion(String numeroCotizacion) { this.numeroCotizacion = numeroCotizacion; }
    
    /** @return Cliente asociado */
    public ClienteEntity getCliente() { return cliente; }
    public void setCliente(ClienteEntity cliente) { this.cliente = cliente; }
    
    /** @return Fecha de creación */
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    /** @return Fecha de vigencia */
    public LocalDate getFechaValidez() { return fechaValidez; }
    public void setFechaValidez(LocalDate fechaValidez) { this.fechaValidez = fechaValidez; }
    
    /** @return Estado actual */
    public EstadoCotizacion getEstado() { return estado; }
    public void setEstado(EstadoCotizacion estado) { this.estado = estado; }
    
    /** @return Descripción */
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    /** @return Subtotal sin impuestos */
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    /** @return Impuestos calculados */
    public BigDecimal getImpuestos() { return impuestos; }
    public void setImpuestos(BigDecimal impuestos) { this.impuestos = impuestos; }
    
    /** @return Total a pagar */
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    /** @return Observaciones */
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    /** @return Items que componen la cotización */
    public List<ItemCotizacionEntity> getItems() { return items; }
    public void setItems(List<ItemCotizacionEntity> items) { this.items = items; }
} 