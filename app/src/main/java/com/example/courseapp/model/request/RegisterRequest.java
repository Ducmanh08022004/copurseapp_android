package com.example.courseapp.model.request;


public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;

    public RegisterRequest(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }
}