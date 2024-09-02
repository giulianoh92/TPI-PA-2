package com.tpi.tpi.repository;

import com.tpi.tpi.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Administrator> findAll() {
        String sql = "SELECT * FROM Users WHERE is_admin = true";

        return jdbcTemplate.query(sql, this::mapRowToAdmin);
    }

    private Administrator mapRowToAdmin(ResultSet rs, int rowNum) throws SQLException {
        return new Administrator(
            rs.getInt("user_id"),
            rs.getString("password"),
            rs.getString("username"),
            rs.getDate("reg_date")
        );
    }
    
}
