package com.uptc.queenscorner.security.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación y generación de tokens JWT.
 * 
 * Endpoints:
 * - POST /api/auth/login: Autentica usuario y retorna token JWT
 * 
 * CORS habilitado para:
 * - http://localhost:4200 (Angular frontend desarrollo)
 * - http://localhost:3000 (Otras aplicaciones frontend)
 * 
 * Todas las respuestas incluyen objeto LoginResponse con:
 * - success: Indicador de éxito/fallo
 * - message: Descripción del resultado
 * - token: JWT si exitoso, null si falló
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AuthController {

    /**
     * Servicio de autenticación
     */
    private final AuthService authService;

    /**
     * Constructor con inyección de AuthService
     * 
     * @param authService Servicio de autenticación
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de login: autentica usuario y retorna JWT.
     * 
     * Método HTTP: POST
     * Ruta: /api/auth/login
     * 
     * Solicitud JSON:
     * ```json
     * {
     *   "username": "admin",
     *   "password": "admin123"
     * }
     * ```
     * 
     * Flujo:
     * 1. Valida que username no esté vacío
     * 2. Valida que password no esté vacío
     * 3. Llama a authService.autenticar()
     * 4. Si exitoso: retorna token en LoginResponse
     * 5. Si falla: retorna error 401 con mensaje de excepción
     * 
     * Respuesta exitosa (200 OK):
     * ```json
     * {
     *   "success": true,
     *   "message": "Login exitoso",
     *   "token": "eyJhbGc...(JWT completo)..."
     * }
     * ```
     * 
     * Respuesta error credenciales (401 UNAUTHORIZED):
     * ```json
     * {
     *   "success": false,
     *   "message": "Credenciales incorrectas",
     *   "token": null
     * }
     * ```
     * 
     * Respuesta error validación (400 BAD REQUEST):
     * ```json
     * {
     *   "success": false,
     *   "message": "El usuario es requerido",
     *   "token": null
     * }
     * ```
     * 
     * @param loginRequest DTO con username y password
     * @return ResponseEntity con LoginResponse y status HTTP apropiado
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar campos requeridos
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                LoginResponse response = new LoginResponse(false, "El usuario es requerido", null);
                return ResponseEntity.badRequest().body(response);
            }
            
            if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                LoginResponse response = new LoginResponse(false, "La contraseña es requerida", null);
                return ResponseEntity.badRequest().body(response);
            }

            // Autenticar usuario
            String token = authService.autenticar(loginRequest.getUsername().trim(), loginRequest.getPassword());
            
            // Retornar token exitosamente
            LoginResponse response = new LoginResponse(true, "Login exitoso", token);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            // Capturar excepciones de validación
            String message = e.getMessage();
            if (message == null) {
                message = "Error durante la autenticación";
            }
            LoginResponse response = new LoginResponse(false, message, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            // Capturar excepciones no esperadas
            LoginResponse response = new LoginResponse(false, "Error interno del servidor", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 