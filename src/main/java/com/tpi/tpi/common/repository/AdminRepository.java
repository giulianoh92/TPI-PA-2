package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for managing Administrator entities.
 */
@Repository
public class AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds all administrators.
     * @return a list of administrators.
     */
    public List<Administrator> findAll() {
        String sql = "SELECT * FROM Users WHERE is_admin = true";
        return jdbcTemplate.query(sql, this::mapRowToAdmin);
    }

    /**
     * Maps a row from the ResultSet to an Administrator object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return an Administrator object.
     * @throws SQLException if a database access error occurs.
     */
    private Administrator mapRowToAdmin(ResultSet rs, int rowNum) throws SQLException {
        return new Administrator(
            rs.getInt("user_id"),
            rs.getString("password"),
            rs.getString("username"),
            rs.getDate("reg_date")
        );
    }
}