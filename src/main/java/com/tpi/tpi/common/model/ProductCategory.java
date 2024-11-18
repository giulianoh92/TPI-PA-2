package com.tpi.tpi.common.model;

/**
 * representa una categoria de producto
 */
public class ProductCategory {
    private int categoryId;
    private String category;

    public ProductCategory(int categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category; // Return only the name for display in JComboBox
    }
}