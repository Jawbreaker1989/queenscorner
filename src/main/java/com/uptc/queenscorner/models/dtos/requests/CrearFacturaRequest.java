package com.uptc.queenscorner.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CrearFacturaRequest {
    
    private Long negocioId;
    private Long cotizacionId;
    private LocalDate fechaVencimiento;
    private String medioPago;
    private String referenciaPago;
    private String notas;
    private String condicionesPago;
    private List<LineaRequest> lineas;

    public static class LineaRequest {
        private Long itemCotizacionId;
        private String descripcion;
        private Integer cantidad;
        private BigDecimal valorUnitario;

        public Long getItemCotizacionId() { return itemCotizacionId; }
        public void setItemCotizacionId(Long itemCotizacionId) { this.itemCotizacionId = itemCotizacionId; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
    }

    public Long getNegocioId() { return negocioId; }
    public void setNegocioId(Long negocioId) { this.negocioId = negocioId; }
    public Long getCotizacionId() { return cotizacionId; }
    public void setCotizacionId(Long cotizacionId) { this.cotizacionId = cotizacionId; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }
    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String referenciaPago) { this.referenciaPago = referenciaPago; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public String getCondicionesPago() { return condicionesPago; }
    public void setCondicionesPago(String condicionesPago) { this.condicionesPago = condicionesPago; }
    public List<LineaRequest> getLineas() { return lineas; }
    public void setLineas(List<LineaRequest> lineas) { this.lineas = lineas; }
}
