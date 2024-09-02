package com.tpi.tpi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.CartService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.view.AdminView;
import com.tpi.tpi.view.CartView;
import com.tpi.tpi.view.ProductView;
import com.tpi.tpi.view.TableView;
import com.tpi.tpi.view.UserView;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    // Define a bean for the views map
    @Bean
    public Map<String, TableView> views() {
        Map<String, TableView> views = new HashMap<>();
        views.put("admin", new AdminView());
        views.put("product", new ProductView());
        views.put("user", new UserView());
        views.put("cartId", new CartView()); // Corrected mapping
        return views;
    }

    // Define a bean for the AdminOperationsController
    @Bean
    public AdminOperationsController adminOperationsController(AdminService adminService, ProductService productService, UserService userService, CartService cartService, Map<String, TableView> views) {
        AdminOperationsController controller = new AdminOperationsController(adminService, productService, userService, cartService, views);
        // Set the controller for the AdminView
        ((AdminView) views.get("admin")).setController(controller);
        return controller;
    }   
}