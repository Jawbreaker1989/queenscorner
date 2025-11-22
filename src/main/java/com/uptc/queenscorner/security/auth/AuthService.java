package com.uptc.queenscorner.security.auth;

import com.uptc.queenscorner.security.jwt.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación y generación de tokens JWT.
 * 
 * Responsabilidades:
 * - Validar credenciales de usuario
 * - Generar tokens JWT después de autenticación exitosa
 * - Mantener lógica de negocio de autenticación centralizada
 * 
 * Nota: Actualmente valida solo usuario hardcodeado (admin/admin123).
 * En producción, buscar usuario en BD y comparar password hasheada.
 */
@Service
public class AuthService {

    /**
     * Utilidad para operaciones JWT (generación, validación, extracción)
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor con inyección de JwtUtil
     * 
     * @param jwtUtil Instancia para generar/validar tokens
     */
    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Autentica un usuario y genera token JWT.
     * 
     * Flujo:
     * 1. Valida que username y password sean correctos
     * 2. Si son válidos: genera y retorna token JWT
     * 3. Si son inválidos: lanza RuntimeException con mensaje
     * 
     * Validación (hardcodeada):
     * - username: "admin"
     * - password: "admin123"
     * 
     * Token generado:
     * - Válido por 24 horas
     * - Contiene username como subject
     * - Firmado con HS256 y clave secreta
     * 
     * @param username Nombre de usuario a autenticar
     * @param password Contraseña a validar
     * @return Token JWT generado exitosamente
     * @throws RuntimeException si credenciales son incorrectas
     */
    public String autenticar(String username, String password) {
        if (validarCredenciales(username, password)) {
            return jwtUtil.generarToken(username);
        }
        throw new RuntimeException("Credenciales incorrectas");
    }

    /**
     * Valida credenciales contra usuario hardcodeado.
     * 
     * NOTA CRÍTICA:
     * Esta es una validación temporal para desarrollo.
     * En producción, debe:
     * 1. Buscar usuario en BD por username
     * 2. Recuperar password hasheada almacenada
     * 3. Comparar password enviada contra hash usando BCrypt
     * 4. Retornar true solo si coinciden exactamente
     * 
     * Credenciales actuales (solo para desarrollo):
     * - Usuario: admin
     * - Contraseña: admin123
     * 
     * @param username Usuario a verificar
     * @param password Contraseña a verificar
     * @return true si credenciales son válidas, false en caso contrario
     */
    private boolean validarCredenciales(String username, String password) {
        return "admin".equals(username) && "admin123".equals(password);
    }
} 