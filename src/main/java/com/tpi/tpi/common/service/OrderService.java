package com.tpi.tpi.common.service;

import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Order;
import com.tpi.tpi.common.model.Payment;
import com.tpi.tpi.common.model.Status;
import com.tpi.tpi.common.repository.OrderRepository;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private static final Logger LOGGER = Logger.getLogger(OrderRepository.class.getName());


    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            LOGGER.severe("Error fetching all orders: " + e.getMessage());
            throw new RuntimeException("Error fetching all orders", e);
        }
    }

    public List<Item> findItemsByOrderId(int orderId) {
        List<Item> items = orderRepository.findItemsByOrderId(orderId);
        LOGGER.info(String.format("Items fetched from repository for order %d: %s", orderId, items));
        return items;
    }

    public List<Order> getOrdersByUserId(int userId) {
        try {
            List<Order> orders = orderRepository.findByUserId(userId);
            return orders;
        } catch (Exception e) {
            LOGGER.severe("Error fetching orders by user ID: " + e.getMessage());
            throw new RuntimeException("Error fetching orders by user ID", e);
        }
    }

    public List<Status> getAllStatuses() {
        try {
            return orderRepository.findAllStatuses();
        } catch (Exception e) {
            LOGGER.severe("Error fetching all statuses: " + e.getMessage());
            throw new RuntimeException("Error fetching all statuses", e);
        }
    }

    public void updateOrder(Order order) {
        try {
            orderRepository.updateOrder(order);
        } catch (Exception e) {
            LOGGER.severe("Error updating order: " + e.getMessage());
            throw new RuntimeException("Error updating order", e);
        }
    }

    public List<Payment> getAllPaymentMethods() {
        try {
            return orderRepository.findAllPaymentMethods();
        } catch (Exception e) {
            LOGGER.severe("Error fetching all payment methods: " + e.getMessage());
            throw new RuntimeException("Error fetching all payment methods", e);
        }
    }

    public void createOrder(Order order, int userId) {
        try {
            orderRepository.addOrder(order, userId);
        } catch (Exception e) {
            LOGGER.severe("Error creating order: " + e.getMessage());
            throw new RuntimeException("Error creating order", e);
        }
    }
}