package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Cliente
 * Representa los clientes del sistema
 * Almacena información de contacto e identificación
 * 
 * Tabla: clientes
 * Estado: Los clientes se marcan como inactivos en lugar de eliminarse (soft delete)
 */
@Entity
@Table(name = "clientes")
public class ClienteEntity {
    
    /** Identificador único en la BD */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del cliente */
    @Column(nullable = false)
    private String nombre;

    /** Número de identificación (cédula o RUC) */
    private String documento;
    
    /** Correo electrónico para contacto */
    private String email;
    
    /** Número de teléfono de contacto */
    private String telefono;
    
    /** Dirección física */
    private String direccion;
    
    /** Ciudad o localidad */
    private String ciudad;

    /** Fecha y hora de creación del registro */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /** Flag de estado (true = activo, false = inactivo/eliminado) */
    private Boolean activo;

    /**
     * Constructor por defecto
     * Inicializa fecha de creación y estado activo
     */
    public ClienteEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    // ===== GETTERS Y SETTERS =====
    
    /** @return ID único del cliente */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    /** @return Nombre completo del cliente */
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    /** @return Número de documento de identificación */
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    
    /** @return Correo electrónico */
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    /** @return Teléfono de contacto */
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    /** @return Dirección física */
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    /** @return Ciudad o localidad */
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    
    /** @return Fecha de creación del registro */
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    /** @return Estado del cliente (activo/inactivo) */
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
} 