package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes_trabajo")
public class OrdenTrabajoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "negocio_id", nullable = false)
    private NegocioEntity negocio;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private PrioridadOrden prioridad;

    @Column(name = "fecha_inicio_estimada")
    private LocalDate fechaInicioEstimada;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    private String observaciones;

    public OrdenTrabajoEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        this.prioridad = PrioridadOrden.MEDIA;
    }

    public enum EstadoOrden {
        PENDIENTE, EN_PROCESO, PAUSADA, FINALIZADA, ENTREGADA
    }

    public enum PrioridadOrden {
        BAJA, MEDIA, ALTA, URGENTE
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NegocioEntity getNegocio() { return negocio; }
    public void setNegocio(NegocioEntity negocio) { this.negocio = negocio; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public PrioridadOrden getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadOrden prioridad) { this.prioridad = prioridad; }
    public LocalDate getFechaInicioEstimada() { return fechaInicioEstimada; }
    public void setFechaInicioEstimada(LocalDate fechaInicioEstimada) { this.fechaInicioEstimada = fechaInicioEstimada; }
    public LocalDate getFechaFinEstimada() { return fechaFinEstimada; }
    public void setFechaFinEstimada(LocalDate fechaFinEstimada) { this.fechaFinEstimada = fechaFinEstimada; }
    public LocalDateTime getFechaEntregaReal() { return fechaEntregaReal; }
    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) { this.fechaEntregaReal = fechaEntregaReal; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}