package com.tpi.tpi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private String direccion;
    private String emailUsuario;
    private Carrito carrito;
    private List<Pedido> pedidos;

    public Cliente(String direccion, int id, String password, String emailUsuario, String nombreUsuario, Date fechaRegistro) {
        super(id, password, nombreUsuario, fechaRegistro);
        this.direccion = direccion;
        this.emailUsuario = emailUsuario;
        this.carrito = new Carrito(); 
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

    public Carrito getCarrito() {
        return carrito;
    }

    public void addToCarrito(Item item) {
        carrito.addItem(item);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }
}
