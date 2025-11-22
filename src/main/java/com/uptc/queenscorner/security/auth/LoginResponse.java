package com.uptc.queenscorner.security.auth;

/**
 * DTO para respuesta de autenticación (login).
 * 
 * Se envía al cliente tras intentar login en /api/auth/login
 * Contiene: estado de éxito, mensaje descriptivo, y token JWT si es exitoso.
 * 
 * Campos:
 * - success: true si autenticación exitosa, false si falló
 * - message: Mensaje descriptivo (ej: "Login exitoso" o "Credenciales incorrectas")
 * - token: JWT válido por 24 horas si success=true; null si success=false
 */
public class LoginResponse {
    
    /**
     * Indica si la autenticación fue exitosa
     * - true: Credenciales válidas, token generado
     * - false: Credenciales inválidas o error en servidor
     */
    private boolean success;
    
    /**
     * Mensaje descriptivo de la respuesta.
     * Ejemplos:
     * - "Login exitoso"
     * - "Credenciales incorrectas"
     * - "El usuario es requerido"
     * - "Error interno del servidor"
     */
    private String message;
    
    /**
     * Token JWT para autenticación posterior.
     * 
     * Validez:
     * - 24 horas desde emisión
     * - Se incluye en header: Authorization: Bearer <token>
     * - null si success=false
     */
    private String token;
    
    /**
     * Constructor sin argumentos para serialización
     */
    public LoginResponse() {}
    
    /**
     * Constructor con todos los parámetros.
     * 
     * @param success Estado de la autenticación
     * @param message Mensaje descriptivo
     * @param token JWT generado (null si falló)
     */
    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }
    
    /**
     * Obtiene el estado de éxito de la autenticación.
     * 
     * @return true si login exitoso, false si falló
     */
    public boolean isSuccess() { return success; }
    
    /**
     * Establece el estado de éxito.
     * 
     * @param success true/false
     */
    public void setSuccess(boolean success) { this.success = success; }
    
    /**
     * Obtiene el mensaje descriptivo.
     * 
     * @return Mensaje de la respuesta
     */
    public String getMessage() { return message; }
    
    /**
     * Establece el mensaje descriptivo.
     * 
     * @param message Mensaje
     */
    public void setMessage(String message) { this.message = message; }
    
    /**
     * Obtiene el token JWT.
     * 
     * @return Token de autenticación (null si falló)
     */
    public String getToken() { return token; }
    
    /**
     * Establece el token JWT.
     * 
     * @param token JWT generado
     */
    public void setToken(String token) { this.token = token; }
} 