package com.tpi.tpi.controller;

import java.util.Map;

import javax.swing.JOptionPane;

import com.tpi.tpi.model.Cart;
import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.CartService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.service.UserService;
import com.tpi.tpi.view.CartView;
import com.tpi.tpi.view.TableView;

public class AdminOperationsController {
    private final AdminService adminService;
    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;
    private final Map<String, TableView> views;

    // Constructor to initialize the services and views
    public AdminOperationsController(AdminService adminService, ProductService productService, UserService userService, CartService cartService, Map<String, TableView> views) {
        this.adminService = adminService;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
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
                case "cartId":
                    int id = CartView.getCartId();
                    Cart cart = this.findCart(id);
                    if (cart != null) {
                        view.showDetails(cart.getItems());
                    } else {
                        JOptionPane.showMessageDialog(null, "Cart not found.");
                    }
                    break;
            }
        }
    }

    // Method to find the cart by id
    public Cart findCart(int id) {
        try {
            Cart cart = cartService.getCartById(id);
            if (cart != null) {
                return cart;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid cart ID: " + id);
        }
        return null;
    }
}