package com.tpi.tpi.controller;

import java.sql.Date;
import java.util.Map;

import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.service.OrderService;
import com.tpi.tpi.service.CustomerService;
import com.tpi.tpi.controller.ViewType;
import com.tpi.tpi.model.Customer;
import com.tpi.tpi.view.PanelView;

public class AdminOperationsController {
    private final AdminService adminService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final Map<ViewType, PanelView> views;

    // Constructor to initialize the services and views
    public AdminOperationsController(AdminService adminService, ProductService productService, UserService userService, OrderService orderService, CustomerService customerService, Map<ViewType, PanelView> views) {
        this.adminService = adminService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.views = views;
    }

    // Helper method to convert Object[] to Customer
    private Customer convertToCustomer(Object[] row) {
        return new Customer((String) row[4], (int) row[0], (String) row[3], (String) row[2], (String) row[1], (Date) row[5]);
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

    public OrderService getOrderService() {
        return orderService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    // Method to handle the commit based on the view type
    public void commitData(ViewType viewType, Object[][] data) {
        PanelView view = views.get(viewType);
        if (view != null) {
            view.handleCommit(data);
        }
    }

    // Method to handle the commit for product data
    public void commitProductData(Object[][] data) {
        // Specific commit logic for product data
        System.out.println("Committing product data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    // Method to handle the commit for user data
    public void commitUserData(Object[][] data) {
        // Specific commit logic for user data
        System.out.println("Committing user data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    // Method to handle the commit for order data
    public void commitOrderData(Object[][] data) {
        // Specific commit logic for order data
        System.out.println("Committing order data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }

        }
    }

    // Method to handle the commit for customer data
    public void commitCustomerData(Object[][] data) {
        // Specific commit logic for customer data
        System.out.println("Committing customer data:");
        for (Object[] row : data) {
            Customer customer = convertToCustomer(row);
            customerService.updateCustomer(customer);
        }
    }

    // Method to handle the commit for admin data
    public void commitAdminData(Object[][] data) {
        // Specific commit logic for admin data
        System.out.println("Committing admin data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }


}