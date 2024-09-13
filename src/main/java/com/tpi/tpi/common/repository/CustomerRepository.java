package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for managing Customer entities.
 */
@Repository
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds all customers.
     * @return a list of customers.
     */
    public List<Customer> findAll() {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id";
        try {
            return jdbcTemplate.query(sql, this::mapRowToCustomer);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all customers", e);
        }
    }

    /**
     * Finds a customer by its ID.
     * @param id the customer ID.
     * @return the customer.
     */
    public Customer findById(int id) {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id WHERE c.customer_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToCustomer, id);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching customer by ID", e);
        }
    }

    /**
     * Updates a customer.
     * @param customer the customer to update.
     */
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
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating customer", e);
        }
    }

    /**
     * Finds a customer by an order ID.
     * @param orderId the order ID.
     * @return the customer.
     */
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
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching customer by order ID", e);
        }
    }

    /**
     * Maps a row from the ResultSet to a Customer object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return a Customer object.
     * @throws SQLException if a database access error occurs.
     */
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
        // Logic to save customer to the database
    }

    public Customer findByEmail(String email) {
        // Logic to find customer by email
        return null; // Replace with actual database query
    }
}