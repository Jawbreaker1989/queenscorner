package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entity que representa una línea individual de detalle dentro de una Factura.
 * 
 * Una línea de factura contiene un servicio o producto específico que se cobra al cliente.
 * Cada factura está compuesta por una o más líneas de detalle donde se especifican:
 * - Descripción del servicio/producto
 * - Cantidad
 * - Precio unitario
 * - Total de la línea (calculado automáticamente)
 * 
 * Características principales:
 * - Vinculada a una Factura específica (ManyToOne)
 * - Número secuencial dentro de la factura (ej: 1, 2, 3...)
 * - Total se calcula automáticamente: cantidad * valor unitario
 * - Permite descripción extensa (hasta 500 caracteres) para detalles del servicio
 * 
 * Nota importante:
 * El campo "total" es calculado automáticamente por la BD mediante una expresión
 * y no se puede modificar directamente (insertable = false, updatable = false)
 * Para calcular el total en la aplicación, usar el método calcularTotal()
 */
@Entity
@Table(name = "lineas_factura", indexes = {
    @Index(name = "idx_lineas_factura_factura_id", columnList = "factura_id")
})
public class LineaFacturaEntity {

    /**
     * Identificador único auto-generado de la línea
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia a la Factura a la que pertenece esta línea.
     * Relación ManyToOne: una factura puede tener múltiples líneas
     * Se carga bajo demanda (LAZY) para no cargar todas las líneas innecesariamente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private FacturaEntity factura;

    /**
     * Número secuencial de la línea dentro de la factura.
     * Comienza en 1 y se incrementa para cada línea
     * Ejemplo: Si una factura tiene 3 líneas, serán: 1, 2, 3
     */
    @Column(name = "numero_linea", nullable = false)
    private Integer numeroLinea;

    /**
     * Descripción del servicio o producto facturado.
     * Texto extenso que explica qué se está cobrando
     * Ejemplo: "Consultoría técnica - 8 horas", "Licencia software anual", etc.
     */
    @Column(nullable = false, length = 500)
    private String descripcion;

    /**
     * Cantidad de unidades del servicio/producto.
     * Puede ser horas (para servicios), unidades (para productos), etc.
     * Se multiplica por el valor unitario para obtener el total
     */
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Precio unitario del servicio o producto.
     * Puede ser el costo por hora, costo por unidad, etc.
     * Total = cantidad * valor unitario
     */
    @Column(name = "valor_unitario", precision = 15, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    /**
     * Total de la línea (cantidad * valor unitario).
     * Este campo es calculado automáticamente por la base de datos.
     * No se puede modificar directamente (insertable=false, updatable=false)
     * Es de solo lectura desde la aplicación
     */
    @Column(name = "total", insertable = false, updatable = false, precision = 15, scale = 2)
    private BigDecimal total;

    /**
     * Constructor por defecto sin parámetros
     */
    public LineaFacturaEntity() {
    }

    /**
     * Calcula el total de esta línea en la aplicación.
     * 
     * Fórmula: cantidad * valor unitario
     * 
     * Nota: La BD también calcula este valor automáticamente.
     * Este método se usa desde la lógica de negocio cuando se necesita
     * calcular totales sin consultar la BD.
     * 
     * @return El total calculado de esta línea
     */
    public BigDecimal calcularTotal() {
        return BigDecimal.valueOf(cantidad).multiply(valorUnitario);
    }



    // ==================== GETTERS Y SETTERS ====================

    /**
     * @return El identificador único de la línea
     */
    public Long getId() { return id; }
    
    /**
     * @param id El nuevo identificador
     */
    public void setId(Long id) { this.id = id; }

    /**
     * @return La factura a la que pertenece esta línea
     */
    public FacturaEntity getFactura() { return factura; }
    
    /**
     * @param factura La factura a vincular
     */
    public void setFactura(FacturaEntity factura) { this.factura = factura; }

    /**
     * @return El número secuencial de esta línea en la factura
     */
    public Integer getNumeroLinea() { return numeroLinea; }
    
    /**
     * @param numeroLinea El número de línea (ej: 1, 2, 3)
     */
    public void setNumeroLinea(Integer numeroLinea) { this.numeroLinea = numeroLinea; }

    /**
     * @return La descripción del servicio o producto
     */
    public String getDescripcion() { return descripcion; }
    
    /**
     * @param descripcion La descripción a establecer
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /**
     * @return La cantidad de unidades
     */
    public Integer getCantidad() { return cantidad; }
    
    /**
     * @param cantidad La cantidad a establecer
     */
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    /**
     * @return El precio unitario del servicio o producto
     */
    public BigDecimal getValorUnitario() { return valorUnitario; }
    
    /**
     * @param valorUnitario El precio unitario a establecer
     */
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    /**
     * @return El total de la línea (solo lectura, calculado por BD)
     */
    public BigDecimal getTotal() { return total; }
}