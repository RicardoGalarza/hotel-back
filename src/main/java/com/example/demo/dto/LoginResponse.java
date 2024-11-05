package com.example.demo.dto;

public class LoginResponse {
    private Long userId;
    private String message;

    // Constructor
    public LoginResponse(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    // Getters y setters
    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
