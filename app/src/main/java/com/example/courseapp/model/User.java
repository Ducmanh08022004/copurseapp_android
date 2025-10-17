package com.example.courseapp.model;

import java.io.Serializable;

public class User implements Serializable {


    private int userId;


    private String username;


    private String fullname;


    private String email;


    private String role;


    private String password;


    public int getId() { return userId; }
    public void setId(int id) { this.userId = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullname; }
    public void setFullName(String fullName) { this.fullname = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

