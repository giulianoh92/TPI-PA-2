package com.tpi.tpi.controller;

import java.util.Map;

import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.view.PanelView;

public class AdminOperationsController {
    private final AdminService adminService;
    private final ProductService productService;
    private final UserService userService;
    private final Map<ViewType, PanelView> views;

    // Constructor to initialize the services and views
    public AdminOperationsController(AdminService adminService, ProductService productService, UserService userService, Map<ViewType, PanelView> views) {
        this.adminService = adminService;
        this.productService = productService;
        this.userService = userService;
        this.views = views;
    }

    // Method to display the table based on the view type
    public void displayView(ViewType viewType) {
        PanelView view = views.get(viewType);
        if (view != null) {
            view.showPanel(this);
        }
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public UserService getUserService() {
        return userService;
    }
}