package com.tpi.tpi.service;

import com.tpi.tpi.model.Order;
import com.tpi.tpi.model.Status;
import com.tpi.tpi.repository.OrderRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all orders", e);
        }
    }

    public List<Order> getOrdersByUserId(int id) {
        try {
            return orderRepository.findByUserId(id);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching orders by user ID", e);
        }
    }

    public List<Status> getAllStatuses() {
        try {
            return orderRepository.findAllStatuses();
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all statuses", e);
        }
    }

    public void updateOrder(Order order) {
        try {
            orderRepository.updateOrder(order);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating order", e);
        }
    }
}