package com.enterprise.workflow.controller;

import com.enterprise.workflow.dto.*;
import com.enterprise.workflow.entity.User;
import com.enterprise.workflow.security.JwtUtil;
import com.enterprise.workflow.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    
    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole(), user.getEmail(), user.getName()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        User user = authService.getCurrentUser(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole(), user.getEmail(), user.getName()));
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        User user = authService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("id", user.getId(), "name", user.getName(), 
                "email", user.getEmail(), "role", user.getRole()));
    }
}