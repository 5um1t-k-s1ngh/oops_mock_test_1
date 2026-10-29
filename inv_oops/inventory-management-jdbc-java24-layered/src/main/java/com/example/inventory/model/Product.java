package com.example.inventory.model;

public class Product {
    private int productId;
    private String productName;
    private String category;
    private int quantity;
    private double price;

    public Product() {
    }

    public Product(int productId, String productName, String category,
                   int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getInventoryValue() {
        return quantity * price;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Category: %s | Quantity: %d | Price: %.2f",
                productId, productName, category, quantity, price);
    }
}
