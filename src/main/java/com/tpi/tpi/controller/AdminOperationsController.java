package com.tpi.tpi.controller;

import java.util.Map;

import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.view.TableView;

public class AdminOperationsController {
    private final AdminService adminService;
    private final ProductService productService;
    private final UserService userService;
    private final Map<String, TableView> views;

    // Constructor to initialize the services and views
    public AdminOperationsController(AdminService adminService, ProductService productService, UserService userService, Map<String, TableView> views) {
        this.adminService = adminService;
        this.productService = productService;
        this.userService = userService;
        this.views = views;
    }

    // Method to display the table based on the view type
    public void displayTable(String viewType) {
        TableView view = views.get(viewType);
        if (view != null) {
            switch (viewType) {
                case "admin":
                    view.showDetails(adminService.getAllAdminList());
                    break;
                case "product":
                    view.showDetails(productService.getAllProducts());
                    break;
                case "user":
                    view.showDetails(userService.getAllUserList());
                    break;
            }
        }
    }
}