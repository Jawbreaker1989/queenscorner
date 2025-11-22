package com.uptc.queenscorner.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por request.
 * 
 * Responsabilidades:
 * - Extraer token JWT del header Authorization
 * - Validar token y extraer usuario
 * - Establecer contexto de seguridad de Spring
 * - Permitir que request continúe si token válido
 * 
 * Ubicación del token:
 * Header: Authorization: Bearer <token_jwt>
 * 
 * Flujo por cada request:
 * 1. Extrae header Authorization
 * 2. Verifica que comience con "Bearer "
 * 3. Extrae token JWT (substring después de "Bearer ")
 * 4. Extrae username desde el token
 * 5. Si username existe y no hay autenticación actual:
 *    a. Valida que el token sea válido
 *    b. Crea UsernamePasswordAuthenticationToken
 *    c. Establece autenticación en SecurityContext
 * 6. Continúa al siguiente filtro/handler
 * 
 * NOTA: No comprueba autorización (roles), solo que el token sea válido.
 * La autorización se configura en SecurityConfig.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Utilidad para validar y extraer datos de tokens JWT
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor con inyección de JwtUtil
     * 
     * @param jwtUtil Instancia para operaciones JWT
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Procesa cada request HTTP para validar y establecer autenticación JWT.
     * 
     * Se ejecuta una vez por request (herencia de OncePerRequestFilter)
     * intercepción ocurre antes de llegar a controladores.
     * 
     * Flujo detallado:
     * 1. Obtiene header "Authorization" del request
     * 2. Si header contiene "Bearer ":
     *    - Extrae el token (substring después de "Bearer ")
     *    - Intenta extraer username del token
     * 3. Si username existe y SecurityContext no tiene autenticación:
     *    - Valida que el token sea válido
     *    - Crea token de autenticación de Spring
     *    - Establece detalles de autenticación web
     *    - Coloca en SecurityContextHolder para uso posterior
     * 4. Continúa con siguiente filtro sin interrumpir
     * 
     * Si token es inválido o no existe:
     * - Se continúa sin autenticación (request anónimo)
     * - Los controladores deben validar según @PreAuthorize
     * 
     * @param request HTTP request actual
     * @param response HTTP response
     * @param chain Cadena de filtros
     * @throws ServletException si hay error en servlet
     * @throws IOException si hay error de I/O
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        
        // Obtener header Authorization (formato: "Bearer <token>")
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Extraer JWT del header si existe y tiene formato correcto
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrae token: substring desde posición 7 ("Bearer " tiene 7 caracteres)
            jwt = authorizationHeader.substring(7);
            // Intenta extraer username del token
            username = jwtUtil.extraerUsername(jwt);
        }

        // Si se extrajo username y no hay autenticación actual
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Valida que el token sea genuino y no esté expirado
            if (jwtUtil.validarToken(jwt)) {
                // Crea token de autenticación para Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,              // principal (usuario)
                    null,                  // credentials (null porque ya fue autenticado)
                    null);                 // authorities (roles/permisos, null por ahora)
                
                // Establece detalles de la request web (IP, session ID, etc)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Coloca el token en el contexto de seguridad de Spring
                // Ahora Spring sabe que este request está autenticado
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continúa con el siguiente filtro en la cadena
        chain.doFilter(request, response);
    }
} 