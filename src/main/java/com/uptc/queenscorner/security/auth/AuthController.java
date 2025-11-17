package com.uptc.queenscorner.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.autenticar(loginRequest.getUsername(), loginRequest.getPassword());
            
            if (token != null) {
                LoginResponse response = new LoginResponse(true, "Login exitoso", token);
                return ResponseEntity.ok(response);
            } else {
                LoginResponse response = new LoginResponse(false, "Credenciales incorrectas", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            LoginResponse response = new LoginResponse(false, "Error en el servidor", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}