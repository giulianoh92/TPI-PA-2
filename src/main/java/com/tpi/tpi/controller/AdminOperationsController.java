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
    @SuppressWarnings("rawtypes")
    private Map<ViewType, PanelView> views;

    public AdminOperationsController(AdminService adminService, ProductService productService, UserService userService, OrderService orderService, CustomerService customerService, @SuppressWarnings("rawtypes") Map<ViewType, PanelView> views) {
        this.adminService = adminService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.views = views;
    }

    @SuppressWarnings("unchecked")
    public void displayView(ViewType viewType) {
        @SuppressWarnings("rawtypes")
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

    public void commitData(ViewType viewType, Object[][] data) {
        @SuppressWarnings("rawtypes")
        PanelView view = views.get(viewType);
        if (view != null) {
            view.handleCommit(data);
        }
    }

    public void commitProductData(List<Product> products) {
        //System.out.println("Committing product data:");
        for (Product product : products) {
            try {
                productService.updateProduct(product);
            } catch (Exception e) {
                // Log the exception and handle it accordingly
                System.err.println("Error committing product data for product ID: " + product.getProductId());
                e.printStackTrace();
            }
        }
    }

    public void addProduct(Product product) {
        try {
            productService.addProduct(product);
        } catch (Exception e) {
            // Log the exception and handle it accordingly
            System.err.println("Error adding product");
            e.printStackTrace();
        }
    }

    public void commitUserData(List<User> users) {
        //System.out.println("Committing user data:");
        for (User user : users) {
            try {
                userService.updateUser(user);
            } catch (Exception e) {
                // Log the exception and handle it accordingly
                System.err.println("Error committing user data for user ID: " + user.getUserId());
                e.printStackTrace();
            }
        }
    }

    public void commitOrderData(List<Order> orders) {
        //System.out.println("Committing order data:");
        for (Order order : orders) {
            try {
                orderService.updateOrder(order);
            } catch (Exception e) {
                // Log the exception and handle it accordingly
                System.err.println("Error committing order data for order ID: " + order.getOrderId());
                e.printStackTrace();
            }
        }
    }

    public void commitCustomerData(List<Customer> customers) {
        //System.out.println("Committing customer data:");
        for (Customer customer : customers) {
            try {
                customerService.updateCustomer(customer);
            } catch (Exception e) {
                // Log the exception and handle it accordingly
                System.err.println("Error committing customer data for customer ID: " + customer.getUserId());
                e.printStackTrace();
            }
        }
    }

    public void commitAdminData(Object[][] data) {
        //System.out.println("Committing admin data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        // Implement the logic to handle admin data commit
    }
}