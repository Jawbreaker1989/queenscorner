package com.uptc.queenscorner.models.dtos.responses;

import java.time.LocalDateTime;

/**
 * DTO que retorna los datos de un cliente.
 * 
 * Se envía al cliente como respuesta cuando:
 * - Se crea un nuevo cliente
 * - Se consulta un cliente
 * - Se actualiza un cliente
 * 
 * Contiene la información completa del cliente incluyendo
 * información de auditoría (fechaCreacion, estado).
 */
public class ClienteResponse {
    /** ID único del cliente */
    private Long id;
    /** Nombre o razón social del cliente */
    private String nombre;
    /** Correo electrónico del cliente */
    private String email;
    /** Teléfono de contacto */
    private String telefono;
    /** Dirección física */
    private String direccion;
    /** Ciudad del cliente */
    private String ciudad;
    /** Fecha de creación en el sistema */
    private LocalDateTime fechaCreacion;
    /** Estado del cliente (ACTIVO/INACTIVO) */
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
} 