package com.example.courseapp.model.response;


import com.example.courseapp.model.User;

public class LoginResponse {
    private String token;
    private User user;

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
