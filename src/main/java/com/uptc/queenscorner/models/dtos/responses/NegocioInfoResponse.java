package com.uptc.queenscorner.models.dtos.responses;

import java.math.BigDecimal;

public class NegocioInfoResponse {
    private Long id;
    private String codigo;
    private String descripcion;
    private ClienteInfoResponse cliente;
    private BigDecimal presupuestoAsignado;

    public NegocioInfoResponse() {
    }

    public NegocioInfoResponse(Long id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public ClienteInfoResponse getCliente() { return cliente; }
    public void setCliente(ClienteInfoResponse cliente) { this.cliente = cliente; }

    public BigDecimal getPresupuestoAsignado() { return presupuestoAsignado; }
    public void setPresupuestoAsignado(BigDecimal presupuestoAsignado) { this.presupuestoAsignado = presupuestoAsignado; }
}
