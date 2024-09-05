package com.tpi.tpi.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.tpi.tpi.model.ProductCategory;
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
 
    // Helper method to convert Object[] to Customer
    private Customer convertToCustomer(Object[] row) {
        Date regDate = null;
        if (row[5] instanceof String) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                regDate = new Date(dateFormat.parse((String) row[5]).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (row[5] instanceof Date) {
            regDate = (Date) row[5];
        }
        return new Customer((int) row[0], // id
                (String) row[1], // name
                (String) row[2], // email
                (String) row[3], // password
                (String) row[4], // address
                regDate); // regDate
    }
 
    // Métodos de conversión
    private Product convertToProduct(Object[] row) {
        return new Product(
            (int) row[0], // id
            (String) row[1], // name
            (String) row[2], // description
            row[3] instanceof String ? Float.parseFloat((String) row[3]) : (Float) row[3], // unitPrice
            row[4] instanceof String ? Integer.parseInt((String) row[4]) : (Integer) row[4], // stock
            new ProductCategory((int) row[5], (String) row[6]) // category
        );
    }

    private User convertToUser(Object[] row) {
        Date regDate = null;
        if (row[3] instanceof String) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                regDate = new Date(dateFormat.parse((String) row[3]).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (row[3] instanceof Date) {
            regDate = (Date) row[3];
        }
        return new User((int) row[0], // id
                        (String) row[1], // name
                        (String) row[2], // password
                        regDate); // regDate
    }
/* 
    private Order convertToOrder(Object[] row) {
        // Implementar lógica de conversión para Orden
        return new Order();
    }*/
/* 
    private Admin convertToAdmin(Object[] row) {
        // Implementar lógica de conversión para Admin
        return new Admin((int) row[0], (String) row[1], (String) row[2]);
    }
*/
    // Métodos de commit actualizados
    public void commitProductData(Object[][] data) {
        System.out.println("Committing product data:");
        for (Object[] row : data) {
            Product product = convertToProduct(row);
            productService.updateProduct(product);
        }
    }

    public void commitUserData(Object[][] data) {
        System.out.println("Committing user data:");
        for (Object[] row : data) {
            User user = convertToUser(row);
            //print user
            System.out.println(user.getIdUsuario() + " " + user.getNombreUsuario() + " " + user.getPassword() + " " + user.getFechaRegistro());
            userService.updateUser(user);
        }
    }
      
    public void commitOrderData(Object[][] data) {
        System.out.println("Committing order data:");
        for (Object[] row : data) {
            //Order order = convertToOrder(row);
            //orderService.updateOrder(order);
        }
    }

    public void commitAdminData(Object[][] data) {
        System.out.println("Committing admin data:");
        for (Object[] row : data) {
            //Admin admin = convertToAdmin(row);
            //adminService.updateAdmin(admin);
        }
    }

    public void commitCustomerData(Object[][] data) {
        System.out.println("Committing customer data:");
        for (Object[] row : data) {
            Customer customer = convertToCustomer(row);
            customerService.updateCustomer(customer);
        }
    }


}