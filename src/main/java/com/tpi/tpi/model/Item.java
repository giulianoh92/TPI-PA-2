package com.tpi.tpi.model;

/**
 * Represents an item in the cart.
 */
public class Item {
    private int amount;
    private Product product;

    public Item(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}