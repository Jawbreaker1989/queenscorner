package com.uptc.queenscorner.security.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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