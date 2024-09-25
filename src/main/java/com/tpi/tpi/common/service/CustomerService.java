package com.tpi.tpi.common.service;

import com.tpi.tpi.common.model.Customer;
import com.tpi.tpi.common.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return passwordEncoder.matches(password, customer.getPassword());
        }
        return false;
    }

    public List<Customer> getAllCustomerList() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all customers", e);
        }
    }

    public void updateCustomer(Customer customer) {
        try {
            customerRepository.updateCustomer(customer);
        } catch (Exception e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    public void registerCustomer(String username, String email, String password, String address) throws Exception {
        if (customerRepository.findByEmail(email) != null) {
            throw new Exception("Email already in use");
        }
        if (customerRepository.findByUsername(username) != null) {
            throw new Exception("Username already in use");
        }
        Customer customer = new Customer(
            0, // userId will be auto-generated
            username,
            email,
            passwordEncoder.encode(password),
            address,
            new Date(System.currentTimeMillis())
        );
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new Exception("Error saving customer: " + e.getMessage());
        }
    }

    public Customer getCustomerByOrderId(int orderId) {
        try {
            return customerRepository.getCustomerByOrderId(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by order id", e);
        }
    }

    public void saveCustomer(Customer customer) {
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("Error saving customer", e);
        }
    }

    public boolean authenticateCustomer(String email, String password) {
        return authenticate(email, password);
    }
}