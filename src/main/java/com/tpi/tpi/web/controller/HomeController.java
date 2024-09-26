package com.tpi.tpi.web.controller;

import com.tpi.tpi.common.model.Customer;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Order;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;
import com.tpi.tpi.common.service.CustomerService;
import com.tpi.tpi.common.service.OrderService;
import com.tpi.tpi.common.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CustomerService customerService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    public HomeController(ProductService productService, OrderService orderService, CustomerService customerService) {
        this.productService = productService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String searchQuery,
            Model model,
            HttpServletRequest request,
            Principal principal) {

        populateModelWithProductsAndCategories(categoryId, minPrice, maxPrice, searchQuery, model);
        populateModelWithUserDetailsIfPresent(principal, model);

        if (isAjaxRequest(request)) {
            return "fragments/product-grid :: productGrid";
        }

        return "home";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-details";
    }

    @GetMapping("/account")
    public String accountDetails(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            logger.info("User logged in: {}", email); // Log the email
    
            Customer customer = customerService.getCustomerByEmail(email);
    
            // Check if customer data is null or has issues
            if (customer == null) {
                logger.warn("No customer found for email: {}", email);
                model.addAttribute("error", "Customer information not found.");
                return "error-page"; // Redirect or return an error page
            }
    
            // Log customer information if needed
            logger.info("Customer details retrieved: {}", customer);
    
            model.addAttribute("customer", customer);
        } else {
            logger.warn("Principal is null.");
            model.addAttribute("error", "User is not logged in.");
            return "error-page"; // Redirect or show an error
        }
        return "account-details"; // Ensure this matches the template name
    }

    private void populateModelWithProductsAndCategories(Integer categoryId, Float minPrice, Float maxPrice, String searchQuery, Model model) {
        List<Product> products = productService.getFilteredProducts(categoryId, minPrice, maxPrice, searchQuery);
        List<ProductCategory> categories = productService.getAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
    }

    private void populateModelWithUserDetailsIfPresent(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            List<Order> orders = orderService.getOrdersByUserId(customerService.getCustomerByEmail(email).getUserId());
            model.addAttribute("orders", orders);
    
            Customer customer = customerService.getCustomerByEmail(email);
            
            // Check for null customer before adding to model
            if (customer != null) {
                model.addAttribute("customer", customer);
            } else {
                logger.warn("No customer found for email: {}", email);
                model.addAttribute("error", "Customer information not found.");
            }
        }
    }

    @GetMapping("/my-orders")
    public String myOrders(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            List<Order> orders = orderService.getOrdersByUserId(customerService.getCustomerByEmail(email).getUserId());
            logger.info("Fetched orders for user {}: {}", email, orders);
    
            // Fetch items for each order and add to the order object
            for (Order order : orders) {
                List<Item> items = orderService.findItemsByOrderId(order.getOrderId());
                order.addItemList(items); 
            }
    
            model.addAttribute("orders", orders);
        } else {
            logger.warn("Principal is null.");
            model.addAttribute("error", "User is not logged in.");
            return "error-page"; // Redirect or show an error
        }
        return "my-orders"; // Ensure this matches the template name
    }
    

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
