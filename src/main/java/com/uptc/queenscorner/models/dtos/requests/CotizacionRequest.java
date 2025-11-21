package com.uptc.queenscorner.models.dtos.requests;

import java.time.LocalDate;
import java.util.List;

public class CotizacionRequest {
    private Long clienteId;
    private LocalDate fechaValidez;
    private String descripcion;
    private String observaciones;
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