package com.uptc.queenscorner.security.auth;

import com.uptc.queenscorner.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Autentica usuario con credenciales hardcodeadas (simple)
     */
    public String autenticar(String username, String password) {
        // Usuario hardcodeado para prueba
        if ("admin".equals(username) && "admin123".equals(password)) {
            return jwtUtil.generarToken(username);
        }
        return null; // Credenciales inválidas
    }
}