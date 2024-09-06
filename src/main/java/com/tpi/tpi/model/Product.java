package com.tpi.tpi.model;

/**
 * Represents a product.
 */
public class Product {
    private int productId;
    private String name;
    private String description;
    private float unitPrice;
    private int stock;
    private ProductCategory category;

    public Product(int productId, String name, String description, float unitPrice, int stock, ProductCategory category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }
}