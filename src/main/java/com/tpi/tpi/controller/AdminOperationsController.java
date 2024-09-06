package com.tpi.tpi.controller;

import java.util.List;
import java.util.Map;

import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.service.OrderService;
import com.tpi.tpi.service.CustomerService;
import com.tpi.tpi.model.Customer;
import com.tpi.tpi.model.Order;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.User;
import com.tpi.tpi.view.PanelView;

public class AdminOperationsController {
    private AdminService adminService;
    private ProductService productService;
    private UserService userService;
    private OrderService orderService;
    private CustomerService customerService;
    private Map<ViewType, PanelView> views;

    // Constructor to initialize the services and views
    public AdminOperationsController(AdminService adminService, ProductService productService, UserService userService, OrderService orderService, CustomerService customerService, Map<ViewType, PanelView> views) {
        this.adminService = adminService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.views = views;
    }

    // Method to display the table based on the view type
    public void displayView(ViewType viewType) {
        PanelView view = views.get(viewType);
        if (view != null) {
            view.showPanel(this);
        }
    }

    // Getters for the services
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

    // Métodos de commit actualizados
    public void commitProductData(List<Product> products) {
        System.out.println("Committing product data:");
        for (Product product : products) {
            productService.updateProduct(product);
        }
    }

    public void commitUserData(List<User> users) {
        System.out.println("Committing user data:");
        for (User user : users) {
            System.out.println(user.getUserId() + " " + user.getUsername() + " " + user.getPassword() + " " + user.getRegisterDate());
            userService.updateUser(user);
        }
    }

    public void commitOrderData(List<Order> orders) {
        System.out.println("Committing order data:");
        for (Order order : orders) {
            order.printAttributes();
            orderService.updateOrder(order);
        }
    }

    public void commitCustomerData(List<Customer> customers) {
        System.out.println("Committing customer data:");
        for (Customer customer : customers) {
            System.out.println(customer);
            customerService.updateCustomer(customer);
        }
    }

    // Add this method to handle admin data commit
    public void commitAdminData(Object[][] data) {
        System.out.println("Committing admin data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        // Implement the logic to handle admin data commit
    }
}