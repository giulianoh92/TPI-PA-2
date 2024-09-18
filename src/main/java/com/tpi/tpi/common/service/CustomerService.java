package com.tpi.tpi.common.service;

import com.tpi.tpi.common.model.Customer;
import com.tpi.tpi.common.repository.CustomerRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomerList() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all customers", e);
        }
    }

    public void updateCustomer(Customer customer) {
        try {
            customerRepository.updateCustomer(customer);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating customer", e);
        }
    }

    public Customer getCustomerByOrderId(int orderId) {
        try {
            return customerRepository.getCustomerByOrderId(orderId);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching customer by order id", e);
        }
    }

    public void saveCustomer(Customer customer) {
        // Logic to save customer, e.g., customerRepository.save(customer);
    }

    public boolean authenticateCustomer(String email, String password) {
        // Logic to authenticate customer, e.g., check email and password in the database
        return true; // Replace with actual authentication logic
    }
}