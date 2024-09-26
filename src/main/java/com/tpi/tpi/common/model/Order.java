package com.tpi.tpi.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order.
 */
public class Order {
    private int orderId;
    private Status status;
    private Payment payment;
    private List<Item> items;

    public Order(int orderId, Status status, Payment payment) {
        this.orderId = orderId;
        this.status = status;
        this.payment = payment;
        this.items = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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

    public void setItems(List<Item> items) {
        this.items = items;
    }


    public void printAttributes() {
        /*System.out.println("Order ID: " + idPedido);
        System.out.println("Status ID: " + estado.getId());
        System.out.println("Status: " + estado.getEstado());
        System.out.println("Payment: " + pago.getMetodoDePago());
        System.out.println("Date: " + pago.getFechaDePago());*/ //in a single line
        System.out.println(orderId + status.getStatusId() + status.getStatus() + payment.getPaymentMethod() + payment.getPaymentDate());
    }
}