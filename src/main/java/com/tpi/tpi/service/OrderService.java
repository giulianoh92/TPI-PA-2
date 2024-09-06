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
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(int id) {
        return orderRepository.findByUserId(id);
    }

    public List<Status> getAllStatuses() {
        return orderRepository.findAllStatuses();
    }

    public void updateOrder(Order order) {
        orderRepository.updateOrder(order);
    }
}
