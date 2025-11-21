package com.uptc.queenscorner.models.dtos.requests;

public class EstadoUpdateRequest {
    private String estado;
    private String observaciones;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
} 