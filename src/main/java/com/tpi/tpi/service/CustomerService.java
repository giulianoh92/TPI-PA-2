package com.tpi.tpi.service;

import com.tpi.tpi.model.Customer;
import com.tpi.tpi.repository.CustomerRepository;

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
}