package com.tpi.tpi.model;

/**
 * Represents an item in the cart.
 */
public class Item {
    private int cantidad;
    private Product producto;

    public Item(int cantidad, Product producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }
}