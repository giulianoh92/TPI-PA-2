package com.tpi.tpi.common.model;
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

    public void addItem(Item newItem) {
        for (Item item : items) {
            if (item.getProduct().getProductId() == newItem.getProduct().getProductId()) {
                item.setAmount(newItem.getAmount()); // Set the quantity directly
                return;
            }
        }
        items.add(newItem);
    }

    public void addItemList(List<Item> items) {
        this.items.addAll(items);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void removeItem(Item itemToRemove) {
        items.removeIf(item -> item.getProduct().getProductId() == itemToRemove.getProduct().getProductId());
    }

    public void clearCart() {
        this.items.clear();
    }

    public float getTotal() {
        return (float)items.stream().mapToDouble(item -> (float)item.getTotal()).sum();   
    }

    public int getTotalItems() {
        return items.size();
    }
}