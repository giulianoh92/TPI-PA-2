package com.tpi.tpi.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the system.
 */
public class Customer extends User {
    private String address;
    private String email;
    private final Cart cart;
    private final List<Order> orders;

    public Customer(int userId, String username, String email, String password, String address, Date registerDate) {
        super(userId, password, username, registerDate);
        this.address = address;
        this.email = email;
        this.cart = new Cart();
        this.orders = new ArrayList<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cart getCart() {
        return cart;
    }

    public void addToCarrito(Item item) {
        cart.addItem(item);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addPedido(Order pedido) {
        this.orders.add(pedido);
    }
}