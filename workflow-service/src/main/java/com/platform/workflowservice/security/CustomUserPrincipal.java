package com.platform.workflowservice.security;

public class CustomUserPrincipal {

    private final String email;
    private final String role;

    public CustomUserPrincipal(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
