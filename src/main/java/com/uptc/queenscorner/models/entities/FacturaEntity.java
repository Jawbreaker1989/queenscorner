package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity que representa una Factura en el sistema.
 * 
 * Una factura es un documento legal que formaliza la venta de productos/servicios
 * a un cliente. Se genera cuando un negocio ha sido completado y contiene detalles
 * de los servicios prestados, valores y montos a cobrar.
 * 
 * Características principales:
 * - Se crea a partir de un Negocio aprobado
 * - Contiene múltiples líneas de detalle con servicios/productos
 * - Calcula automáticamente subtotales e IVA (19%)
 * - Mantiene estado de envío (ENVIADA por defecto)
 * - Puede generar PDF para envío al cliente
 * - Almacena información de auditoría (usuario que crea/envía, fechas)
 * 
 * Relaciones:
 * - Muchas facturas pueden pertenecer a un Negocio (ManyToOne)
 * - Una factura referencia la Cotización original (ManyToOne, opcional)
 * - Una factura contiene múltiples Líneas de Factura (OneToMany)
 * 
 * Índices creados para optimizar consultas:
 * - idx_facturas_negocio_id: buscar facturas por negocio
 * - idx_facturas_numero: buscar factura por número único
 * - idx_facturas_estado: filtrar por estado
 * - idx_facturas_fecha_creacion: ordenar y filtrar por fecha
 */
@Entity
@Table(name = "facturas", indexes = {
    @Index(name = "idx_facturas_negocio_id", columnList = "negocio_id"),
    @Index(name = "idx_facturas_numero", columnList = "numero_factura"),
    @Index(name = "idx_facturas_estado", columnList = "estado"),
    @Index(name = "idx_facturas_fecha_creacion", columnList = "fecha_creacion")
})
public class FacturaEntity {

    /**
     * Identificador único auto-generado de la factura
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de factura único en el sistema.
     * Formato: FAC-AÑO-000000 (ej: FAC-2024-000123)
     * Se genera automáticamente cuando la factura se emite
     */
    @Column(unique = true, nullable = false, length = 50)
    private String numeroFactura;

    /**
     * Código interno para identificación alternativa.
     * Generado con timestamp al crear la factura: COD-<timestamp>
     */
    @Column(nullable = false, length = 255)
    private String codigo = "";

    /**
     * Fecha de creación del registro en la base de datos.
     * Se establece automáticamente al crear la factura
     */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Fecha de emisión de la factura.
     * Inicialmente es igual a la fecha de creación.
     * Puede ser actualizada si la emisión se pospone
     */
    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    /**
     * Fecha en que la factura fue enviada al cliente.
     * Nulo hasta que se marca como "enviada"
     */
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    /**
     * Relación con el Negocio que genera esta factura.
     * Cada factura debe estar vinculada a un negocio específico.
     * Se carga inmediatamente (EAGER) para reportes y validaciones
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "negocio_id", nullable = false)
    private NegocioEntity negocio;

    /**
     * Relación con la Cotización original.
     * Referencia la cotización que originó el negocio y esta factura.
     * Se carga bajo demanda (LAZY) para no sobrecargar consultas
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cotizacion_id")
    private CotizacionEntity cotizacion;

    /**
     * Líneas de detalle de la factura.
     * Contiene productos/servicios con cantidad, precio unitario y total.
     * Se elimina automáticamente cuando la factura es eliminada (cascade).
     * Carga bajo demanda para mejorar rendimiento
     */
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineaFacturaEntity> lineas = new ArrayList<>();

    /**
     * Suma total de todos los artículos en líneas (sin IVA).
     * Se calcula automáticamente llamando a calcularTotales()
     */
    @Column(name = "subtotal_items", precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotalItems = BigDecimal.ZERO;

    /**
     * Anticipo o pago adelantado realizado por el cliente.
     * Se descuenta del total a pagar
     */
    @Column(name = "anticipo", precision = 15, scale = 2, nullable = false)
    private BigDecimal anticipo = BigDecimal.ZERO;

    /**
     * Subtotal de la factura (antes de impuestos).
     * Se calcula de la suma de líneas: sum(cantidad * precioUnitario)
     */
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    /**
     * Impuesto al valor agregado (IVA 19%).
     * Se calcula automáticamente: subtotal * 0.19
     */
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal iva = BigDecimal.ZERO;

    /**
     * Campo específico para IVA al 19%.
     * Puede usarse para validación o reportes específicos
     */
    @Column(name = "iva_19", precision = 15, scale = 2)
    private BigDecimal iva19;

    /**
     * Total a pagar por el cliente.
     * Incluye: subtotal + IVA - anticipo
     */
    @Column(name = "total_a_pagar", precision = 15, scale = 2)
    private BigDecimal totalAPagar;

    /**
     * Total de la factura (subtotal + IVA).
     * Se calcula automáticamente sin considerar anticipo
     */
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    /**
     * Estado de la factura.
     * Valor por defecto: "ENVIADA"
     * Estados posibles: ENVIADA, EN_REVISION, PAGADA, etc.
     */
    @Column(nullable = false, length = 50)
    private String estado = "ENVIADA";

    /**
     * Notas u observaciones adicionales sobre la factura.
     * Texto libre para información relevante que no cabe en otros campos
     */
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    /**
     * Usuario que creó el registro de la factura.
     * Información de auditoría para rastrear quién originou el documento
     */
    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    /**
     * Usuario que marcó la factura como enviada.
     * Información de auditoría para rastrear quién envió el documento
     */
    @Column(name = "usuario_envio", length = 100)
    private String usuarioEnvio;

    /**
     * Ruta o ubicación del archivo PDF generado de la factura.
     * Se guarda después de generar el PDF para envío por correo
     */
    @Column(name = "path_pdf", length = 500)
    private String pathPdf;

    /**
     * Constructor por defecto que inicializa los valores por defecto.
     * Establece:
     * - Fechas de creación y emisión al momento actual
     * - Código con timestamp único
     * - Estado por defecto: ENVIADA
     * - Todos los montos en cero inicialmente
     */
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

    /**
     * Calcula los totales de la factura basado en sus líneas.
     * 
     * Proceso:
     * 1. Suma el total de cada línea (cantidad * precioUnitario)
     * 2. Calcula el subtotal
     * 3. Calcula el IVA (19% del subtotal)
     * 4. Calcula el total final (subtotal + IVA)
     * 
     * Se debe llamar después de modificar las líneas.
     */
    public void calcularTotales() {
        BigDecimal subtotalCalculado = lineas.stream()
            .map(LineaFacturaEntity::calcularTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.subtotal = subtotalCalculado;
        this.iva = subtotalCalculado.multiply(BigDecimal.valueOf(0.19));
        this.total = subtotalCalculado.add(this.iva);
    }

    /**
     * Genera automáticamente el número de factura con formato único.
     * 
     * Formato: FAC-AAAA-CCCCCC
     * - AAAA: Año de la fecha de creación
     * - CCCCCC: Número secuencial de 6 dígitos (ej: 000001, 000123)
     * 
     * @param consecutivo Número secuencial único para este año
     */
    public void generarNumeroFactura(long consecutivo) {
        int anio = this.fechaCreacion.getYear();
        this.numeroFactura = String.format("FAC-%d-%06d", anio, consecutivo);
    }

    /**
     * Cambia el estado de la factura a "enviada" y registra auditoría.
     * 
     * Actualiza:
     * - Estado a ENVIADA
     * - Usuario que envió
     * - Fecha de envío al momento actual
     * 
     * @param usuario Nombre o ID del usuario que envía la factura
     */
    public void cambiarAEnviada(String usuario) {
        this.usuarioEnvio = usuario;
        this.fechaEnvio = LocalDateTime.now();
    }

    /**
     * Verifica si la factura puede ser enviada al cliente.
     * 
     * Requisitos para enviar:
     * - Debe tener al menos una línea de detalle
     * - Debe estar vinculada a un Negocio válido
     * 
     * @return true si cumple con los requisitos para envío
     */
    public boolean puedeSerEnviada() {
        return !this.lineas.isEmpty() && this.negocio != null;
    }



    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * @return El identificador único de la factura
     */
    public Long getId() { return id; }
    
    /**
     * @param id El nuevo identificador (normalmente auto-generado)
     */
    public void setId(Long id) { this.id = id; }

    /**
     * @return El número único de factura (FAC-AAAA-CCCCCC)
     */
    public String getNumeroFactura() { return numeroFactura; }
    
    /**
     * @param numeroFactura El número de factura a establecer
     */
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    /**
     * @return La fecha en que se creó el registro en BD
     */
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    
    /**
     * @param fechaCreacion La fecha de creación a establecer
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /**
     * @return La fecha en que la factura fue enviada al cliente
     */
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    
    /**
     * @param fechaEnvio La fecha de envío a establecer
     */
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    /**
     * @return El negocio al que pertenece esta factura
     */
    public NegocioEntity getNegocio() { return negocio; }
    
    /**
     * @param negocio El negocio a vincular con esta factura
     */
    public void setNegocio(NegocioEntity negocio) { this.negocio = negocio; }

    /**
     * @return La lista de líneas de detalle de la factura
     */
    public List<LineaFacturaEntity> getLineas() { return lineas; }
    
    /**
     * @param lineas La nueva lista de líneas
     */
    public void setLineas(List<LineaFacturaEntity> lineas) { this.lineas = lineas; }

    /**
     * @return El subtotal de la factura (sin IVA)
     */
    public BigDecimal getSubtotal() { return subtotal; }
    
    /**
     * @param subtotal El subtotal a establecer
     */
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    /**
     * @return El IVA calculado (19% del subtotal)
     */
    public BigDecimal getIva() { return iva; }
    
    /**
     * @param iva El IVA a establecer
     */
    public void setIva(BigDecimal iva) { this.iva = iva; }

    /**
     * @return El total de la factura (subtotal + IVA)
     */
    public BigDecimal getTotal() { return total; }
    
    /**
     * @param total El total a establecer
     */
    public void setTotal(BigDecimal total) { this.total = total; }

    /**
     * @return El estado actual de la factura
     */
    public String getEstado() { return estado; }
    
    /**
     * @param estado El nuevo estado (ej: ENVIADA, EN_REVISION, PAGADA)
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * @return Observaciones o notas sobre la factura
     */
    public String getObservaciones() { return observaciones; }
    
    /**
     * @param observaciones Las observaciones a establecer
     */
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    /**
     * @return El usuario que creó la factura
     */
    public String getUsuarioCreacion() { return usuarioCreacion; }
    
    /**
     * @param usuarioCreacion El usuario que crea la factura
     */
    public void setUsuarioCreacion(String usuarioCreacion) { this.usuarioCreacion = usuarioCreacion; }

    /**
     * @return El usuario que marcó la factura como enviada
     */
    public String getUsuarioEnvio() { return usuarioEnvio; }
    
    /**
     * @param usuarioEnvio El usuario que envía la factura
     */
    public void setUsuarioEnvio(String usuarioEnvio) { this.usuarioEnvio = usuarioEnvio; }

    /**
     * @return La ruta del PDF generado de la factura
     */
    public String getPathPdf() { return pathPdf; }
    
    /**
     * @param pathPdf La ruta del archivo PDF a establecer
     */
    public void setPathPdf(String pathPdf) { this.pathPdf = pathPdf; }

    /**
     * @return El código interno/timestamp de la factura
     */
    public String getCodigo() { return codigo; }
    
    /**
     * @param codigo El código a establecer
     */
    public void setCodigo(String codigo) { this.codigo = codigo; }

    /**
     * @return La fecha de emisión de la factura
     */
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    
    /**
     * @param fechaEmision La fecha de emisión a establecer
     */
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    /**
     * @return La cotización original que originó esta factura
     */
    public CotizacionEntity getCotizacion() { return cotizacion; }
    
    /**
     * @param cotizacion La cotización a vincular
     */
    public void setCotizacion(CotizacionEntity cotizacion) { this.cotizacion = cotizacion; }

    /**
     * @return La suma total de artículos en líneas
     */
    public BigDecimal getSubtotalItems() { return subtotalItems; }
    
    /**
     * @param subtotalItems El subtotal de artículos a establecer
     */
    public void setSubtotalItems(BigDecimal subtotalItems) { this.subtotalItems = subtotalItems; }

    /**
     * @return El anticipo o pago adelantado realizado
     */
    public BigDecimal getAnticipo() { return anticipo; }
    
    /**
     * @param anticipo El anticipo a establecer
     */
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }

    /**
     * @return El IVA específico al 19%
     */
    public BigDecimal getIva19() { return iva19; }
    
    /**
     * @param iva19 El IVA al 19% a establecer
     */
    public void setIva19(BigDecimal iva19) { this.iva19 = iva19; }

    /**
     * @return El total a pagar (subtotal + IVA - anticipo)
     */
    public BigDecimal getTotalAPagar() { return totalAPagar; }
    
    /**
     * @param totalAPagar El total a pagar a establecer
     */
    public void setTotalAPagar(BigDecimal totalAPagar) { this.totalAPagar = totalAPagar; }
}