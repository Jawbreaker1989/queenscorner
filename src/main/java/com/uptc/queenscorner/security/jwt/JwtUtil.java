package com.uptc.queenscorner.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilidad para operaciones con JSON Web Tokens (JWT).
 * 
 * Responsabilidades:
 * - Generar tokens JWT con datos de usuario
 * - Validar integridad de tokens
 * - Extraer información (username) desde tokens
 * 
 * Parámetros de tokens:
 * - Algoritmo: HS256 (HMAC con SHA-256)
 * - Validez: 24 horas desde emisión
 * - Clave secreta: queenscorner-secret-key-2025-very-secure (hardcodeada)
 * 
 * NOTA IMPORTANTE:
 * En producción, la clave secreta debe:
 * - Almacenarse en variables de entorno
 * - Ser de mínimo 256 bits (32 caracteres)
 * - Rotar periódicamente
 * - Nunca hardcodearse en el código
 */
@Component
public class JwtUtil {
    
    /**
     * Clave secreta para firmar y validar tokens.
     * 
     * CRITICO: Solo para desarrollo.
     * En producción: usar System.getenv("JWT_SECRET_KEY")
     */
    private final SecretKey secretKey = Keys.hmacShaKeyFor("queenscorner-secret-key-2025-very-secure".getBytes());
    
    /**
     * Tiempo de expiración de tokens en milisegundos.
     * Valor: 24 horas = 86400000 ms
     */
    private final long expirationMs = 24 * 60 * 60 * 1000;
    
    /**
     * Genera un nuevo token JWT para un usuario.
     * 
     * Contenido del token:
     * - Subject (sub): username del usuario
     * - Issued At (iat): timestamp de emisión (ahora)
     * - Expiration (exp): timestamp de expiración (+24 horas)
     * - Signature: HMAC-SHA256 con clave secreta
     * 
     * Formato del token: header.payload.signature (base64url encoded)
     * 
     * Ejemplo de payload decodificado:
     * ```json
     * {
     *   "sub": "admin",
     *   "iat": 1700612345,
     *   "exp": 1700698745
     * }
     * ```
     * 
     * @param username Nombre de usuario a incluir como subject
     * @return Token JWT completo listo para usar
     */
    public String generarToken(String username) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Valida la integridad y vigencia de un token JWT.
     * 
     * Validaciones:
     * - Firma del token es válida (no fue modificado)
     * - Token no ha expirado (exp > ahora)
     * - Formato del token es correcto (header.payload.signature)
     * 
     * Nota: Valida estructura pero no autorización (eso es responsabilidad del controlador)
     * 
     * @param token Token JWT a validar
     * @return true si token es válido, false si está expirado o tiene firma inválida
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // Token expirado, modificado, o con formato inválido
            return false;
        }
    }
    
    /**
     * Extrae el username desde un token JWT válido.
     * 
     * Lee el claim "subject" del payload del token.
     * Solo funciona si el token es válido (no expirado y firma correcta).
     * 
     * @param token Token JWT válido
     * @return Username del usuario contenido en el token, o null si token inválido
     */
    public String extraerUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            // Token inválido, expirado, o con formato incorrecto
            return null;
        }
    }
} 