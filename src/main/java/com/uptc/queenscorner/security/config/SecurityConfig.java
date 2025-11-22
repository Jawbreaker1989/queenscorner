package com.uptc.queenscorner.security.config;

import com.uptc.queenscorner.security.jwt.JwtAuthenticationFilter;
import com.uptc.queenscorner.security.jwt.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Configuración centralizada de Spring Security para la aplicación.
 * 
 * Responsabilidades:
 * - Configurar políticas de CORS
 * - Configurar autenticación basada en JWT
 * - Definir qué endpoints requieren autenticación
 * - Configurar filtro JWT en la cadena de seguridad
 * - Deshabilitar CSRF (innecesario con JWT stateless)
 * - Configurar manejo de errores de autenticación
 * 
 * Flujo de Seguridad:
 * 1. CORS: Valida origen de request
 * 2. JwtAuthenticationFilter: Extrae y valida JWT
 * 3. Autorización: Verifica si request requiere autenticación
 * 4. Si falla: JwtAuthenticationEntryPoint maneja error 401
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Manejador de errores de autenticación (401 Unauthorized)
     */
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    /**
     * Utilidad para operaciones con JWT
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor con inyección de dependencias
     * 
     * @param jwtAuthenticationEntryPoint Manejador de errores auth
     * @param jwtUtil Utilidad JWT
     */
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtUtil jwtUtil) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Crea instancia del filtro JWT.
     * 
     * Se ejecuta en cada request para validar token del header Authorization.
     * 
     * @return JwtAuthenticationFilter configurado
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    /**
     * Configura políticas de CORS (Cross-Origin Resource Sharing).
     * 
     * Permite requests desde:
     * - http://localhost:4200 (Angular frontend en desarrollo)
     * - http://localhost:3000 (Otras aplicaciones frontend)
     * 
     * Métodos HTTP permitidos: GET, POST, PUT, PATCH, DELETE, OPTIONS
     * 
     * Headers permitidos: * (todos)
     * 
     * Headers expuestos: Authorization (para que cliente reciba JWT), Content-Type
     * 
     * Credenciales: true (permite cookies/autenticación)
     * 
     * Max-Age: 3600 segundos (1 hora de cache de preflight OPTIONS)
     * 
     * @return Configuración de CORS para toda la aplicación
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000"));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Headers permitidos en request
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Headers expuestos en response (cliente puede leerlos)
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Permitir credenciales (cookies, basic auth)
        configuration.setAllowCredentials(true);
        
        // Cache preflight OPTIONS por 1 hora
        configuration.setMaxAge(3600L);

        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configura la cadena de filtros de seguridad HTTP.
     * 
     * Flujo:
     * 1. CORS: Habilitar CORS con configuración anterior
     * 2. CSRF: Deshabilitar (no necesario con JWT stateless)
     * 3. Exception Handling: Usar JwtAuthenticationEntryPoint para 401
     * 4. Session Management: STATELESS (sin sesiones, cada request autenticado con JWT)
     * 5. Autorización de requests:
     *    - /api/auth/login: Permitir sin autenticación
     *    - /swagger-ui.html, /api-docs: Permitir sin autenticación (documentación)
     *    - /* (resto): Requiere autenticación
     * 6. Agregar filtro JWT a la cadena (antes de UsernamePasswordAuthenticationFilter)
     * 
     * @param http Configurador HTTP de seguridad
     * @return Cadena de filtros configurada
     * @throws Exception si hay error en configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // CSRF: Deshabilitar porque usamos JWT (no necesita protección CSRF)
            .csrf(csrf -> csrf.disable())
            
            // Exception Handling: Usar entry point personalizado para 401
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            
            // Session Management: Stateless (sin sesiones)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Autorización de requests
            .authorizeHttpRequests(auth -> auth
                // Login permitido sin autenticación
                .requestMatchers("/api/auth/login").permitAll()
                
                // Swagger UI permitido sin autenticación (documentación)
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**").permitAll()
                
                // Resto de requests requiere autenticación
                .anyRequest().authenticated()
            );

        // Agregar filtro JWT antes del filtro de username/password
        // Así se valida JWT primero en cada request
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    /**
     * Proporciona el AuthenticationManager para uso en servicios.
     * 
     * El AuthenticationManager es responsable de autenticar usuarios.
     * En este caso, solo lo usamos para obtener la instancia del contexto.
     * 
     * @param config Configuración de autenticación
     * @return AuthenticationManager
     * @throws Exception si hay error
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 