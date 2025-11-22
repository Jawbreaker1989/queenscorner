package com.uptc.queenscorner.models.dtos.requests;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear o actualizar una cotización.
 * 
 * Se utiliza cuando el usuario desea:
 * - Crear una nueva cotización para un cliente
 * - Actualizar una cotización existente (agregando/modificando items)
 * 
 * Contiene:
 * - Referencia al cliente (clienteId)
 * - Fecha de validez de la cotización
 * - Descripción general
 * - Lista de items/servicios
 * - Observaciones adicionales
 */
public class CotizacionRequest {
    /** ID del cliente al que va dirigida la cotización */
    private Long clienteId;
    /** Fecha hasta la cual es válida esta cotización */
    private LocalDate fechaValidez;
    /** Descripción general del proyecto/propuesta */
    private String descripcion;
    /** Notas u observaciones adicionales */
    private String observaciones;
    /** Lista de servicios/productos a cotizar */
    private List<ItemCotizacionRequest> items;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public LocalDate getFechaValidez() { return fechaValidez; }
    public void setFechaValidez(LocalDate fechaValidez) { this.fechaValidez = fechaValidez; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<ItemCotizacionRequest> getItems() { return items; }
    public void setItems(List<ItemCotizacionRequest> items) { this.items = items; }
} 