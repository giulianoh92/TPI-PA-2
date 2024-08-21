package com.tpi.tpi;

import java.util.List;
import java.util.ArrayList;

public class Pedido {
    private List<Item> items;
    private int id_pedido;
    private Estado estado;
    private Pago pago;

    public Pedido(int id_pedido, Estado estado, Pago pago) {
        this.id_pedido = id_pedido;
        this.estado = estado;
        this.pago = pago;
        this.items = new ArrayList<>();
    }

    // Getters y Setters
    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
