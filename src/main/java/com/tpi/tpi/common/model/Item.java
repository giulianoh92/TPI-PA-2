package com.tpi.tpi.common.model;

/**
 * representa un item (producto y cantidad)
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

    public Object getTotal() {
        return amount * product.getUnitPrice();
    }
}