package com.tpi.tpi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.controller.ViewType;
import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.view.AdminView;
import com.tpi.tpi.view.ProductView;
import com.tpi.tpi.view.PanelView;
import com.tpi.tpi.view.UserView;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    // Define a bean for the views map
    @Bean
    public Map<ViewType, PanelView> views() {
        Map<ViewType, PanelView> views = new HashMap<>();
        views.put(ViewType.ADMIN, new AdminView());
        views.put(ViewType.PRODUCT, new ProductView());
        views.put(ViewType.USER, new UserView());
        return views;
    }

    // Define a bean for the AdminOperationsController
    @Bean
    public AdminOperationsController adminOperationsController(AdminService adminService, ProductService productService, UserService userService, Map<ViewType, PanelView> views) {
        AdminOperationsController controller = new AdminOperationsController(adminService, productService, userService, views);
        // Set the controller for the AdminView
        ((AdminView) views.get(ViewType.ADMIN)).setController(controller);
        return controller;
    }   
}