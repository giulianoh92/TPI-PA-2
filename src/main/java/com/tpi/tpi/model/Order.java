package com.tpi.tpi.model;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private List<Item> items;
    private int id_pedido;
    private Status estado;
    private Payment pago;

    public Order(int id_pedido, Status estado, Payment pago) {
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

    public void addItemList(List<Item> items) {
        this.items.addAll(items);
    }

    public Status getEstado() {
        return estado;
    }

    public void setEstado(Status estado) {
        this.estado = estado;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public Payment getPago() {
        return pago;
    }

    public void setPago(Payment pago) {
        this.pago = pago;
    }
}
