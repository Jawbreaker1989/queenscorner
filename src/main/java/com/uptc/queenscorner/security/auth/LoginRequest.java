package com.uptc.queenscorner.security.auth;

/**
 * DTO para solicitud de autenticación (login).
 * 
 * Contiene las credenciales del usuario para autenticarse en el sistema.
 * Se envía en requests POST a /api/auth/login
 * 
 * Campos:
 * - username: Nombre de usuario o email
 * - password: Contraseña del usuario
 */
public class LoginRequest {
    
    /**
     * Nombre de usuario o email del usuario
     */
    private String username;
    
    /**
     * Contraseña del usuario (sin cifrar en el request, se valida en servidor)
     */
    private String password;
    
    /**
     * Constructor sin argumentos para deserialización de JSON
     */
    public LoginRequest() {}
    
    /**
     * Constructor con parámetros.
     * 
     * @param username Nombre de usuario o email
     * @param password Contraseña
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Obtiene el nombre de usuario.
     * 
     * @return username del usuario
     */
    public String getUsername() { return username; }
    
    /**
     * Establece el nombre de usuario.
     * 
     * @param username Nombre de usuario
     */
    public void setUsername(String username) { this.username = username; }
    
    /**
     * Obtiene la contraseña.
     * 
     * @return password
     */
    public String getPassword() { return password; }
    
    /**
     * Establece la contraseña.
     * 
     * @param password Contraseña
     */
    public void setPassword(String password) { this.password = password; }
} 