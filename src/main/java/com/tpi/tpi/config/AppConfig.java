package com.tpi.tpi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.desktop.controller.ViewType;
import com.tpi.tpi.common.service.AdminService;
import com.tpi.tpi.common.service.ProductService;
import com.tpi.tpi.common.service.UserService;
import com.tpi.tpi.common.service.OrderService;
import com.tpi.tpi.common.service.CustomerService;
import com.tpi.tpi.desktop.view.AdminView;
import com.tpi.tpi.desktop.view.ProductView;
import com.tpi.tpi.desktop.view.PanelView;


import com.tpi.tpi.desktop.view.UserView;
import com.tpi.tpi.desktop.view.OrderView;
import com.tpi.tpi.desktop.view.CustomerView;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    // definir los beans para las vistas
    @Bean
    public AdminView adminView() {
        return new AdminView();
    }

    @Bean
    public ProductView productView() {
        return new ProductView();
    }

    @Bean
    public UserView userView() {
        return new UserView();
    }

    @Bean
    public OrderView orderView() {
        return new OrderView();
    }

    @Bean
    public CustomerView customerView() {
        return new CustomerView();
    }

    // definir un bean para el mapa de vistas
    // creamos un mapa de vistas porque necesitamos inyectar todas las vistas en el controlador
    @SuppressWarnings("rawtypes")
    @Bean
    public Map<ViewType, PanelView> views(AdminView adminView, ProductView productView, UserView userView, OrderView orderView, CustomerView customerView) {
        Map<ViewType, PanelView> views = new HashMap<>();
        views.put(ViewType.ADMIN, adminView);
        views.put(ViewType.PRODUCT, productView);
        views.put(ViewType.USER, userView);
        views.put(ViewType.ORDER, orderView);
        views.put(ViewType.CUSTOMER, customerView);
        return views;
    }

    // definir un bean para el controlador y pasarle las dependencias
    @Bean
    public AdminOperationsController adminOperationsController(AdminService adminService, ProductService productService, UserService userService, OrderService orderService, CustomerService customerService, @SuppressWarnings("rawtypes") Map<ViewType, PanelView> views) {
        AdminOperationsController controller = new AdminOperationsController(adminService, productService, userService, orderService, customerService, views);
        // asignar el controlador a la vista
        ((AdminView) views.get(ViewType.ADMIN)).setController(controller);
        return controller;
    }   
}