package com.tpi.tpi.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tpi.tpi.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CustomerRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Customer> findAll() {
        String sql = "SELECT c.*,u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id";

        return jdbcTemplate.query(sql, this::mapRowToCustomer);
    }

    public Customer findById(int id) {
        String sql = "SELECT c.*,u.* FROM Customers c JOIN Users u ON c.customer_id = u.user_id WHERE c.customer_id = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowToCustomer, id);
    }

    public void updateCustomer(Customer customer) {
        String sql = 
            "UPDATE Customers c " +
            "JOIN Users u ON c.customer_id = u.user_id " +
            "SET c.address = ?, c.email = ?, u.username = ?, u.password = ? " +
            "WHERE c.customer_id = ?";
        jdbcTemplate.update(sql, customer.getDireccion(), customer.getEmail(), customer.getNombreUsuario(), customer.getPassword(), customer.getIdUsuario());
    }

    private Customer mapRowToCustomer(ResultSet rs, int rowNum) throws SQLException {
        return new Customer(
            rs.getString("address"),        // direccion
            rs.getInt("customer_id"),       // id
            rs.getString("password"),       // password
            rs.getString("email"),          // emailUsuario
            rs.getString("username"),       // nombreUsuario
            rs.getDate("reg_date")          // fechaRegistro
        );
    }
    
}
