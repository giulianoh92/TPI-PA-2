package com.tpi.tpi.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
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

    // Opcional: métodos adicionales como toString(), equals(), hashCode()
}
