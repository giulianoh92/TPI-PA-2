package com.tpi.tpi.controller;

import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.view.AdminView;
import com.tpi.tpi.view.ProductView;

public class AdminOperationsController {
    private final AdminService adminService;
    private final ProductService productService;
    private final AdminView adminView;
    private final ProductView productView;

    public AdminOperationsController(AdminService adminService, ProductService productService, AdminView adminView, ProductView productView) {
        this.adminService = adminService;
        this.productService = productService;
        this.adminView = adminView;
        this.productView = productView;
    }

    public void displayAdminTable() {
        adminView.showAdminDetails(adminService.getAllAdminList());
    }

    public void displayProductTable() {
        productView.showProductDetails(productService.getAllProducts());
    }
}