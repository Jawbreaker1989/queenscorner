package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que retorna información resumida de un negocio.
 * 
 * Se utiliza en respuestas embebidas (ej: cuando se devuelve una factura,
 * se incluye información del negocio sin necesidad de todos los datos completos).
 * 
 * Contiene información financiera clave:
 * - ID, número y fecha del negocio
 * - Descripción del proyecto
 * - Montos: total cotización, anticipo, saldo pendiente
 * - Información del cliente asociado
 */
public class NegocioInfoResponse {
    
    /** ID único del negocio */
    private Long id;
    /** Número o código del negocio */
    private String numero;
    /** Fecha de creación del negocio */
    private LocalDateTime fechaCreacion;
    /** Nombre/descripción del proyecto */
    private String proyecto;
    /** Total de la cotización original */
    private BigDecimal totalCotizacion;
    /** Anticipo pagado por el cliente */
    private BigDecimal anticipo;
    /** Saldo aún pendiente por cobrar */
    private BigDecimal saldoPendiente;
    /** Información resumida del cliente */
    private ClienteInfoResponse cliente;
    
    public NegocioInfoResponse() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getProyecto() { return proyecto; }
    public void setProyecto(String proyecto) { this.proyecto = proyecto; }
    
    public BigDecimal getTotalCotizacion() { return totalCotizacion; }
    public void setTotalCotizacion(BigDecimal totalCotizacion) { this.totalCotizacion = totalCotizacion; }
    
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    
    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    
    public ClienteInfoResponse getCliente() { return cliente; }
    public void setCliente(ClienteInfoResponse cliente) { this.cliente = cliente; }
} 
