package com.tpi.tpi.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int cartId;
    private List<Item> items = new ArrayList<>();

    public Cart() {
    }

    // Getters y Setters
    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    // Opcional: métodos adicionales como toString(), equals(), hashCode()
}
