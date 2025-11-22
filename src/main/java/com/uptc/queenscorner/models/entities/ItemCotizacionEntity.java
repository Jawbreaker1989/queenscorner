package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity que representa un artículo o línea de detalle dentro de una Cotización.
 * 
 * Un item de cotización es un servicio o producto específico que se incluye en la oferta
 * que se presenta al cliente. Cada cotización está compuesta por uno o más items donde
 * se especifican:
 * - Descripción del servicio/producto
 * - Cantidad requerida
 * - Precio unitario
 * - Subtotal (cantidad * precio unitario)
 * 
 * Características principales:
 * - Vinculado a una Cotización específica (ManyToOne)
 * - Permite describir múltiples servicios/productos en una sola cotización
 * - El subtotal se calcula como: cantidad * precio unitario
 * - Registra fecha de creación para auditoría
 * - Valor inicial: cantidad 1, precios en cero
 */
@Entity
@Table(name = "items_cotizacion")
public class ItemCotizacionEntity {
    
    /**
     * Identificador único auto-generado del item
     */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia a la Cotización a la que pertenece este item.
     * Relación ManyToOne: una cotización puede tener múltiples items
     */
    @ManyToOne
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private CotizacionEntity cotizacion;

    /**
     * Descripción del servicio o producto ofrecido.
     * Texto que explica en detalle qué se está cotizando
     */
    @Column(nullable = false)
    private String descripcion;

    /**
     * Cantidad del servicio/producto.
     * Se multiplica por el precio unitario para obtener el subtotal
     */
    @Column(nullable = false)
    private Integer cantidad;
    
    /**
     * Precio unitario del servicio o producto.
     * Subtotal = cantidad * precio unitario
     */
    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal precioUnitario;
    
    /**
     * Subtotal del item (cantidad * precio unitario).
     * Se calcula al crear/actualizar el item
     */
    @Column(nullable = false)
    private BigDecimal subtotal;

    /**
     * Fecha de creación del item en la cotización.
     * Se establece automáticamente al crear el item
     */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /**
     * Constructor por defecto que inicializa los valores por defecto.
     * Establece:
     * - Fecha de creación al momento actual
     * - Cantidad inicial: 1 unidad
     * - Precio unitario: 0
     * - Subtotal: 0
     */
    public ItemCotizacionEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.cantidad = 1;
        this.precioUnitario = BigDecimal.ZERO;
        this.subtotal = BigDecimal.ZERO;
    }

    // ==================== GETTERS Y SETTERS ====================

    /** @return El identificador único del item */
    public Long getId() { return id; }
    /** @param id El nuevo identificador */
    public void setId(Long id) { this.id = id; }

    /** @return La cotización a la que pertenece este item */
    public CotizacionEntity getCotizacion() { return cotizacion; }
    /** @param cotizacion La cotización a vincular */
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }

    /** @return La descripción del servicio/producto */
    public String getDescripcion() { return descripcion; }
    /** @param descripcion La descripción a establecer */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** @return La cantidad de unidades */
    public Integer getCantidad() { return cantidad; }
    /** @param cantidad La cantidad a establecer */
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    /** @return El precio unitario del servicio/producto */
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    /** @param precioUnitario El precio unitario a establecer */
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    /** @return El subtotal del item (cantidad * precio unitario) */
    public BigDecimal getSubtotal() { return subtotal; }
    /** @param subtotal El subtotal a establecer */
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    /** @return La fecha de creación del item */
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    /** @param fechaCreacion La fecha de creación a establecer */
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
} 