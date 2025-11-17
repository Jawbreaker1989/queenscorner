package com.uptc.queenscorner.security.auth;

import com.uptc.queenscorner.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public String autenticar(String username, String password) {
        if ("admin".equals(username) && "admin123".equals(password)) {
            return jwtUtil.generarToken(username);
        }
        return null;
    }
}