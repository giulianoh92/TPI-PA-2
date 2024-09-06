package com.tpi.tpi.repository;

import com.tpi.tpi.model.Customer;
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
        return jdbcTemplate.query(sql, this::mapRowToCustomer);
    }

    /**
     * Finds a customer by its ID.
     * @param id the customer ID.
     * @return the customer.
     */
    public Customer findById(int id) {
        String sql = "SELECT c.*, u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id WHERE c.customer_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToCustomer, id);
    }

    /**
     * Updates a customer.
     * @param customer the customer to update.
     */
    public void updateCustomer(Customer customer) {
        String sql = 
            "UPDATE Customers c " +
            "JOIN Users u ON c.customer_id = u.user_id " +
            "SET c.address = ?, c.email = ?, u.username = ?, u.password = ? " +
            "WHERE c.customer_id = ?";
        jdbcTemplate.update(sql, customer.getAddress(), customer.getEmail(), customer.getUsername(), customer.getPassword(), customer.getUserId());
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
}