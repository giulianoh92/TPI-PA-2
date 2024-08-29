/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi.model;

/**
 *
 * @author giu
 */
public class Item {
    private int cantidad;
    private Product producto;

    public Item(int cantidad, Product producto){
        this.cantidad = cantidad;
        this.producto = producto;
    }

    //getters y setters
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
