package com.tpi.tpi.web.controller;

import com.tpi.tpi.common.model.Customer;
import com.tpi.tpi.common.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        try {
            // Assuming a method to save customer exists in CustomerService
            customerService.saveCustomer(customer);
            return ResponseEntity.ok("Customer registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error registering customer: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestParam String email, @RequestParam String password) {
        try {
            // Assuming a method to authenticate customer exists in CustomerService
            boolean isAuthenticated = customerService.authenticateCustomer(email, password);
            if (isAuthenticated) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error during login: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("<html><body><h1>Web Application is Running</h1></body></html>");
    }
}