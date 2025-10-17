package com.example.courseapp.model.request;


public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // Không cần getter/setter cho request body, Gson sẽ tự động serialize.
}
