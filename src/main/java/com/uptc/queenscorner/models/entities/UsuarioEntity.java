package com.uptc.queenscorner.models.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity que representa un usuario del sistema QueensCorner.
 * 
 * Los usuarios son las personas que pueden acceder y usar la aplicación.
 * Cada usuario tiene credenciales (usuario/contraseña) y se asigna un rol
 * que determina sus permisos en el sistema.
 * 
 * Características principales:
 * - Username único para identificar el usuario
 * - Contraseña encriptada para autenticación
 * - Email para contacto y recuperación de cuenta
 * - Estado activo/inactivo (desactivación sin eliminar)
 * - Rol que determina permisos (actualmente solo ADMIN)
 * - Auditoría de fechas (creación y última actualización)
 * 
 * Seguridad:
 * - El password debe estar encriptado usando un encoder seguro (BCrypt)
 * - El username es único y case-sensitive en la BD
 * - Un usuario inactivo no puede acceder a la aplicación
 * 
 * Nota: En versiones futuras se pueden agregar más roles
 * (USER, SUPERVISOR, etc.) para implementar control de acceso granular
 */
@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
    
    /**
     * Identificador único auto-generado del usuario
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único en el sistema.
     * Se usa para login junto con la contraseña.
     * No puede haber dos usuarios con el mismo username
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Contraseña del usuario encriptada.
     * Debe almacenarse SIEMPRE encriptada usando BCrypt u otro algoritmo seguro.
     * Nunca se debe guardar en texto plano
     */
    @Column(nullable = false)
    private String password;

    /**
     * Correo electrónico del usuario.
     * Se usa para notificaciones, recuperación de cuenta y contacto
     */
    private String email;

    /**
     * Indica si el usuario está activo o ha sido desactivado.
     * Un usuario inactivo (false) no puede iniciar sesión.
     * Permite "eliminar" usuarios sin borrar sus datos históricos
     */
    private Boolean activo;

    /**
     * Rol del usuario que determina sus permisos en el sistema.
     * Actualmente solo existe el rol ADMIN
     * Se almacena como ENUM en la BD para garantizar valores válidos
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ADMIN')")
    private Rol rol;

    /**
     * Fecha en que se creó el registro del usuario
     */
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    /**
     * Fecha de la última actualización del usuario
     */
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Enumeración de roles disponibles en el sistema.
     * 
     * Roles actuales:
     * - ADMIN: Acceso total a todas las funciones del sistema
     * 
     * Nota: Puede extenderse con USER, SUPERVISOR, CLIENTE, etc.
     */
    public enum Rol {
        ADMIN
    }

    /**
     * Constructor por defecto que inicializa los valores por defecto.
     * Establece:
     * - Fecha de creación y actualización al momento actual
     * - Estado activo: true
     * - Rol por defecto: ADMIN
     */
    public UsuarioEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.activo = true;
        this.rol = Rol.ADMIN; 
    }
    
    // ==================== GETTERS Y SETTERS ====================

    /**
     * @return El identificador único del usuario
     */
    public Long getId() { return id; }
    /**
     * @param id El nuevo identificador
     */
    public void setId(Long id) { this.id = id; }

    /**
     * @return El nombre de usuario (login)
     */
    public String getUsername() { return username; }
    /**
     * @param username El nombre de usuario a establecer
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * @return La contraseña encriptada del usuario
     */
    public String getPassword() { return password; }
    /**
     * @param password La contraseña a establecer (debe estar encriptada)
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * @return El correo electrónico del usuario
     */
    public String getEmail() { return email; }
    /**
     * @param email El correo a establecer
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * @return true si el usuario está activo, false si está desactivado
     */
    public Boolean getActivo() { return activo; }
    /**
     * @param activo El estado del usuario (true: activo, false: inactivo)
     */
    public void setActivo(Boolean activo) { this.activo = activo; }

    /**
     * @return La fecha de creación del usuario
     */
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    /**
     * @param fechaCreacion La fecha de creación a establecer
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /**
     * @return La fecha de la última actualización
     */
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    /**
     * @param fechaActualizacion La fecha de actualización a establecer
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    /**
     * @return El rol del usuario en el sistema
     */
    public Rol getRol() { return rol; }
    /**
     * @param rol El rol a asignar al usuario
     */
    public void setRol(Rol rol) { this.rol = rol; }
} 