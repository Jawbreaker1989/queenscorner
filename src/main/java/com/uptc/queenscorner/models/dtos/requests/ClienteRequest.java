package com.uptc.queenscorner.models.dtos.requests;

/**
 * DTO para recibir información de un cliente desde el cliente web/móvil.
 * 
 * Se utiliza cuando:
 * - Se crea un nuevo cliente
 * - Se actualiza un cliente existente
 * 
 * Contiene solo los campos que el cliente puede proporcionar.
 * Los campos de auditoría (id, fechaCreacion, activo) se generan en el servidor.
 */
public class ClienteRequest {
    /** Nombre comercial o razón social del cliente */
    private String nombre;
    /** Correo electrónico para contacto */
    private String email;
    /** Teléfono de contacto del cliente */
    private String telefono;
    /** Dirección física del cliente */
    private String direccion;
    /** Ciudad/localidad del cliente */
    private String ciudad;

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
} 