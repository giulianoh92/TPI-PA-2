package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.Customer;
import com.tpi.tpi.common.model.Cart;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Customer> findAll() {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id";
        try {
            return jdbcTemplate.query(sql, this::mapRowToCustomer);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all customers", e);
        }
    }

    public Customer findById(int id) {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id WHERE c.customer_id = ?";
        try {
            return jdbcTemplate.query(sql, this::mapRowToCustomer, id).stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by ID", e);
        }
    }

    public void updateCustomer(Customer customer) {
        String sql = 
            """
            UPDATE Customers c \
            JOIN Users u ON c.customer_id = u.user_id \
            SET c.address = ?, c.email = ?, u.username = ?, u.password = ? \
            WHERE c.customer_id = ?\
            """;
        try {
            jdbcTemplate.update(sql, customer.getAddress(), customer.getEmail(), customer.getUsername(), customer.getPassword(), customer.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    public Customer getCustomerByOrderId(int orderId) {
        String sql = 
            """
            SELECT c.*, u.* \
            FROM Customers c \
            JOIN Users u ON c.customer_id = u.user_id \
            JOIN Orders o ON c.customer_id = o.customer_id \
            WHERE o.order_id = ?
            """;
        try {
            return jdbcTemplate.query(sql, this::mapRowToCustomer, orderId).stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by order ID", e);
        }
    }

    private Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer(
            rs.getInt("customer_id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("address"),
            rs.getDate("reg_date")
        );

        // Fetch the cart for the customer
        Cart cart = getCartByCustomerId(customer.getUserId());
        customer.setCart(cart);

        return customer;
    }

    private Cart getCartByCustomerId(int customerId) {
        String sql = "SELECT * FROM Carts INNER JOIN Customers ON Carts.cart_id = Customers.cart_id WHERE customer_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToCart, customerId).stream().findFirst().orElse(null);
    }

    private Cart mapRowToCart(ResultSet rs, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));

        // Fetch items for the cart
        String sql = "SELECT * FROM Items WHERE cart_id = ?";
        List<Item> items = jdbcTemplate.query(sql, this::mapRowToItem, cart.getCartId());
        items.forEach(cart::addItem);

        return cart;
    }

    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        Product product = jdbcTemplate.query(sql, this::mapRowToProduct, rs.getInt("product_id")).stream().findFirst().orElse(null);
        return new Item(rs.getInt("amount"), product);
    }

    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Prod_categories WHERE category_id = ?";
        ProductCategory category = jdbcTemplate.query(sql, this::mapRowToProductCategory, rs.getInt("category_id")).stream().findFirst().orElse(null);
        return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                true,
                category
        );
    }

    private ProductCategory mapRowToProductCategory(ResultSet rs, int rowNum) throws SQLException {
        return new ProductCategory(rs.getInt("category_id"), rs.getString("name"));
    }

    public void save(Customer customer) {
        String sqlUsers = "INSERT INTO Users (username, password, is_admin, reg_date) VALUES (?, ?, false, CURRENT_DATE)";
        String sqlCart = "INSERT INTO Carts () VALUES ()";
        String sqlCustomers = "INSERT INTO Customers (customer_id, email, address, cart_id) VALUES (?, ?, ?, ?)";
    
        try {
            // Insert user
            logger.debug("Inserting user with username: {}", customer.getUsername());
            jdbcTemplate.update(sqlUsers, customer.getUsername(), customer.getPassword());
            Integer userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            if (userId == null) {
                throw new RuntimeException("Failed to retrieve last inserted user ID");
            }
            logger.debug("Inserted user with ID: {}", userId);
    
            // Insert cart
            logger.debug("Inserting cart");
            jdbcTemplate.update(sqlCart);
            Integer cartId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            if (cartId == null) {
                throw new RuntimeException("Failed to retrieve last inserted cart ID");
            }
            logger.debug("Inserted cart with ID: {}", cartId);
    
            // Insert customer with the user ID and cart ID
            logger.debug("Inserting customer with user ID: {} and cart ID: {}", userId, cartId);
            jdbcTemplate.update(sqlCustomers, userId, customer.getEmail(), customer.getAddress(), cartId);
        } catch (Exception e) {
            logger.error("Error saving customer: {}", e.getMessage());
            throw new RuntimeException("Error saving customer", e);
        }
    }

    public Customer findByEmail(String email) {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id WHERE c.email = ?";
        try {
            return jdbcTemplate.query(sql, this::mapRowToCustomer, email).stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by email", e);
        }
    }

    public Customer findByUsername(String username) {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id WHERE u.username = ?";
        try {
            return jdbcTemplate.query(sql, this::mapRowToCustomer, username).stream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by username", e);
        }
    }
}