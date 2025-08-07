package com.s23010843.essentialsmonitor;

public class User {
    private int id;
    private String email;
    private String name;
    private String createdAt;
    private String password;
    private String imageUrl;

    // Constructors
    public User() {}

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public void setPassword(String string) { this.password = string;}

    public void setImageUrl(String string) { this.imageUrl = string; }
    
    public String getPassword() { return password; }
    public String getImageUrl() { return imageUrl; }
}
