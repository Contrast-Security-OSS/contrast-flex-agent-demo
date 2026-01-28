package com.contrast.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Admin User entity - INTENTIONALLY VULNERABLE
 * Demonstrates weak authentication and insecure password storage
 */
@Entity
@Table(name = "admin_users")
public class AdminUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank
    @Column(nullable = false)
    private String password; // VULNERABILITY: Plain text password storage
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column
    private String role = "ADMIN";
    
    // Constructors
    public AdminUser() {}
    
    public AdminUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
