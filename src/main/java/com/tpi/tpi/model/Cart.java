package com.tpi.tpi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping cart.
 */
public class Cart {
    private int cartId;
    private List<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}