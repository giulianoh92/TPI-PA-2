package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for managing User entities.
 */
@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds all users.
     * @return a list of users.
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM Users";
        try {
            return jdbcTemplate.query(sql, this::mapRowToUser);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all users", e);
        }
    }

    /**
     * Finds a user by its ID.
     * @param id the user ID.
     * @return the user.
     */
    public User findById(int id) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching user by ID", e);
        }
    }

    /**
     * Updates a user.
     * @param user the user to update.
     */
    public void updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, password = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getUserId());
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating user", e);
        }
    }

    /**
     * Maps a row from the ResultSet to a User object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return a User object.
     * @throws SQLException if a database access error occurs.
     */
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
            rs.getInt("user_id"),
            rs.getString("password"),
            rs.getString("username"),
            rs.getDate("reg_date")
        );
    }
}