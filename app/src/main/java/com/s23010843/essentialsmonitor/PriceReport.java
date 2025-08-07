package com.s23010843.essentialsmonitor;

public class PriceReport {
    private int id;
    private double price;
    private boolean verified;
    private String createdAt;
    private String productName;
    private String storeName;

    // Constructors
    public PriceReport() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public void setProductId(int productId) { }
    public void setStoreId(int storeId) { }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
}