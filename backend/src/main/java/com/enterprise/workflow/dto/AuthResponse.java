package com.enterprise.workflow.dto;

import com.enterprise.workflow.entity.User;

public class AuthResponse {
    private String token;
    private User.Role role;
    private String email;
    private String name;
    
    public AuthResponse() {}
    
    public AuthResponse(String token, User.Role role, String email, String name) {
        this.token = token;
        this.role = role;
        this.email = email;
        this.name = name;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}