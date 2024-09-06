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
        return customerRepository.findAll();
    }

    public void updateCustomer(Customer row) {
        customerRepository.updateCustomer(row);
    }
}
