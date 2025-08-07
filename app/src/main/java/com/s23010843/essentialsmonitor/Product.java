package com.s23010843.essentialsmonitor;

public class Product {
    private int id;
    private String name, category, brand, description, imageUrl;

    // Constructors
    public Product() {}

    public Product(String name, String category, String brand, String description) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
    }

    public Product(String name, String category, String brand, String description, String imageUrl) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
