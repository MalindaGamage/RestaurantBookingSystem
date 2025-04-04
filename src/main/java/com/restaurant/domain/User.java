package com.restaurant.domain;

public abstract class User {
    private int userId;
    private String username;
    private String password;
    private String role;

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(){
        // Simulate login logic (check credentials)
        return true;
    }

    public void logout() {
        // Simulate logout logic
        System.out.println("User " + username + " logged out.");
    }

    public String getRole() {
        return role;
    }
}
