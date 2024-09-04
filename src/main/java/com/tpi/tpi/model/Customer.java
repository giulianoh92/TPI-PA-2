package com.tpi.tpi.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    private String direccion;
    private String emailUsuario;
    private Cart carrito;
    private List<Order> pedidos;

    public Customer(int id, String nombreUsuario, String emailUsuario, String password, String direccion, Date fechaRegistro) {
        super(id, password, nombreUsuario, fechaRegistro);
        this.direccion = direccion;
        this.emailUsuario = emailUsuario;
        this.carrito = new Cart(); 
        this.pedidos = new ArrayList<>(); 
    }

    // Getters y Setters
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return emailUsuario;
    }

    public void setEmail(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Cart getCarrito() {
        return carrito;
    }

    public void addToCarrito(Item item) {
        carrito.addItem(item);
    }

    public List<Order> getPedidos() {
        return pedidos;
    }

    public void addPedido(Order pedido) {
        this.pedidos.add(pedido);
    }
}
