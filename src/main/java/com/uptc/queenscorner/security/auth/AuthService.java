package com.uptc.queenscorner.security.auth;

import com.uptc.queenscorner.security.jwt.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String autenticar(String username, String password) {
        if (validarCredenciales(username, password)) {
            return jwtUtil.generarToken(username);
        }
        throw new RuntimeException("Credenciales incorrectas");
    }

    private boolean validarCredenciales(String username, String password) {
        return "admin".equals(username) && "admin123".equals(password);
    }
} 