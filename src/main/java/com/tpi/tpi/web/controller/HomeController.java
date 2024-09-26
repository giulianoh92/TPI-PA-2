package com.tpi.tpi.web.controller;

import com.tpi.tpi.common.model.Cart;
import com.tpi.tpi.common.model.Customer;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Order;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;
import com.tpi.tpi.common.service.CartService;
import com.tpi.tpi.common.service.CustomerService;
import com.tpi.tpi.common.service.OrderService;
import com.tpi.tpi.common.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    public HomeController(ProductService productService, OrderService orderService, CustomerService customerService, CartService cartService) {
        this.productService = productService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.cartService = cartService;
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
        if (principal == null) {
            model.addAttribute("errorMessage", "User not logged in");
            return "error"; // Ensure you have an error.html template
        }
        
        String email = principal.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            model.addAttribute("errorMessage", "Customer not found");
            return "error"; // Ensure you have an error.html template
        }
        
        model.addAttribute("customer", customer);
        return "account-details"; // Ensure this matches the template name
    }
    
    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("errorMessage", "User not logged in");
            return "error"; // Ensure you have an error.html template
        }
        
        String email = principal.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            model.addAttribute("errorMessage", "Customer not found");
            return "error"; // Ensure you have an error.html template
        }
        
        Cart cart = customer.getCart();
        model.addAttribute("cartItems", cart.getItems());
        return "cart"; // Ensure this matches the template name
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
    
            // Fetch items for each order and set the items list
            for (Order order : orders) {
                List<Item> items = orderService.findItemsByOrderId(order.getOrderId());
                order.setItems(items); // Ensure this method replaces the existing list
            }
    
            model.addAttribute("orders", orders);
        } else {
            logger.warn("Principal is null.");
            model.addAttribute("error", "User is not logged in.");
            return "error-page"; // Redirect or show an error
        }
        return "my-orders"; // Ensure this matches the template name
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public String addToCart(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            if (principal == null) {
                return "User not logged in";
            }
            String email = principal.getName();
            Customer customer = customerService.getCustomerByEmail(email);
            if (customer == null) {
                return "Customer not found";
            }
    
            // Ensure the customer has a cart
            Cart cart = customer.getCart();
            if (cart == null) {
                cart = new Cart();
                cartService.updateCart(cart); // Save the new cart to get the cart ID
                customer.setCart(cart);
                customerService.updateCustomer(customer); // Update the customer with the new cart
            }
    
            int productId = Integer.parseInt(payload.get("productId").toString());
            int quantity = Integer.parseInt(payload.get("quantity").toString());
    
            Product product = productService.getProductById((long) productId);
            if (product == null) {
                return "Product not found";
            }
    
            Item item = new Item(quantity, product);
            cart.addItem(item);
            cartService.updateCart(cart);
    
            return "Product added to cart";
        } catch (Exception e) {
            logger.error("Error adding product to cart", e);
            return "Error adding product to cart";
        }
    }

    @PostMapping("/cart/update")
    @ResponseBody
    public String updateCart(@RequestBody Map<String, Object> payload, Principal principal) {
        if (principal == null) {
            return "User not authenticated";
        }
        String email = principal.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return "Customer not found";
        }
    
        try {
            int productId = Integer.parseInt(payload.get("productId").toString());
            int quantity = Integer.parseInt(payload.get("quantity").toString());
            Product product = productService.getProductById((long) productId);
            if (product == null) {
                return "Product not found";
            }
    
            Cart cart = customer.getCart();
            Item item = new Item(quantity, product);
            if (quantity <= 0) {
                cart.removeItem(item);
                cartService.removeItemFromCart(item, cart.getCartId());
            } else {
                cart.addItem(item);
                cartService.updateCart(cart);
            }
    
            return "Cart updated";
        } catch (NumberFormatException e) {
            logger.error("Error parsing productId or quantity", e);
            return "Invalid productId or quantity";
        }
    }

    @PostMapping("/cart/clear")
    @ResponseBody
    public String clearCart(Principal principal) {
        if (principal == null) {
            return "User not authenticated";
        }
        String email = principal.getName();
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            return "Customer not found";
        }

        Cart cart = customer.getCart();
        cart.clearCart();
        cartService.updateCart(cart);

        return "Cart cleared";
    }
    
    

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
