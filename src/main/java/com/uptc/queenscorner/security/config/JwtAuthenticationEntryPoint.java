package com.uptc.queenscorner.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador personalizado de errores de autenticación para JWT.
 * 
 * Responsabilidades:
 * - Interceptar requests sin autenticación válida
 * - Retornar response 401 UNAUTHORIZED con mensaje descriptivo
 * - Aplicarse globalmente a todos los endpoints protegidos
 * 
 * Se dispara cuando:
 * - Header Authorization está ausente
 * - Token JWT es inválido o expirado
 * - SecurityContext no tiene autenticación
 * 
 * Response retornado:
 * - Status: 401 UNAUTHORIZED
 * - Body: "Acceso no autorizado"
 * - Content-Type: text/plain
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Maneja el error de autenticación.
     * 
     * Invocado automáticamente por Spring Security cuando un request
     * intenta acceder a recurso protegido sin autenticación válida.
     * 
     * Flujo:
     * 1. Recibe request sin autenticación
     * 2. Recibe response a enviar
     * 3. Recibe excepción de autenticación (con detalles del error)
     * 4. Envía error 401 con mensaje al cliente
     * 
     * NOTA: Este método no retorna respuesta JSON.
     * Para respuesta JSON personalizada, se debe crear custom filter
     * que capture excepciones y envíe JSON.
     * 
     * Exemplo:
     * Request: GET /api/clientes (sin Authorization header)
     * Response:
     * - Status: 401 UNAUTHORIZED
     * - Body: "Acceso no autorizado"
     * 
     * @param request Request HTTP que falló autenticación
     * @param response Response para enviar error
     * @param authException Excepción de autenticación con detalles
     * @throws IOException si hay error de I/O
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Envía error 401 con mensaje descriptivo
        // El response se envía como text/plain, no JSON
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
    }
} 