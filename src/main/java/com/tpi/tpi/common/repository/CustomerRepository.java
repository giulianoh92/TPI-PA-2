package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {

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
            return jdbcTemplate.queryForObject(sql, this::mapRowToCustomer, id);
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
            return jdbcTemplate.queryForObject(sql, this::mapRowToCustomer, orderId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching customer by order ID", e);
        }
    }

    private Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
            rs.getInt("customer_id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("address"),
            rs.getDate("reg_date")
        );
    }

    public void save(Customer customer) {
        String sqlUsers = "INSERT INTO Users (username, password, is_admin, reg_date) VALUES (?, ?, false, CURRENT_DATE)";
        String sqlCustomers = "INSERT INTO Customers (customer_id, email, address) VALUES (?, ?, ?)";
    
        try {
            jdbcTemplate.update(sqlUsers, customer.getUsername(), customer.getPassword());
            Integer userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            if (userId == null) {
                throw new RuntimeException("Failed to retrieve last inserted user ID");
            }
            jdbcTemplate.update(sqlCustomers, userId, customer.getEmail(), customer.getAddress());
        } catch (Exception e) {
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
}