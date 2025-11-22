package com.uptc.queenscorner.models.dtos.responses;

/**
 * DTO para estadísticas del sistema
 */
public class SystemStats {

    private long totalClientes;
    private long totalCotizaciones;
    private long totalNegocios;
    private long totalFacturas;
    private long totalUsuarios;

    public SystemStats() {}

    public SystemStats(long totalClientes, long totalCotizaciones, long totalNegocios,
                      long totalFacturas, long totalUsuarios) {
        this.totalClientes = totalClientes;
        this.totalCotizaciones = totalCotizaciones;
        this.totalNegocios = totalNegocios;
        this.totalFacturas = totalFacturas;
        this.totalUsuarios = totalUsuarios;
    }

    // Getters y Setters
    public long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(long totalClientes) { this.totalClientes = totalClientes; }

    public long getTotalCotizaciones() { return totalCotizaciones; }
    public void setTotalCotizaciones(long totalCotizaciones) { this.totalCotizaciones = totalCotizaciones; }

    public long getTotalNegocios() { return totalNegocios; }
    public void setTotalNegocios(long totalNegocios) { this.totalNegocios = totalNegocios; }

    public long getTotalFacturas() { return totalFacturas; }
    public void setTotalFacturas(long totalFacturas) { this.totalFacturas = totalFacturas; }

    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
}