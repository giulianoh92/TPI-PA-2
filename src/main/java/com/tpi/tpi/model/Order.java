package com.tpi.tpi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order.
 */
public class Order {
    private int idPedido;
    private Status estado;
    private Payment pago;
    private List<Item> items;

    public Order(int idPedido, Status estado, Payment pago) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.pago = pago;
        this.items = new ArrayList<>();
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Status getEstado() {
        return estado;
    }

    public void setEstado(Status estado) {
        this.estado = estado;
    }

    public Payment getPago() {
        return pago;
    }

    public void setPago(Payment pago) {
        this.pago = pago;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addItemList(List<Item> items) {
        this.items.addAll(items);
    }

    public void printAtributes() {
        System.out.println("Order ID: " + idPedido);
        System.out.println("Status: " + estado.getEstado());
        System.out.println("Payment: " + pago.getMetodoDePago());
        System.out.println("Date: " + pago.getFechaDePago());
    }
}