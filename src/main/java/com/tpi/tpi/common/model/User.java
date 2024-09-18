package com.tpi.tpi.common.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Represents a user in the system.
 */
public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
    private Date registerDate;

    public User(int UserId, String password, String username, Date registerDate) {
        this.userId = UserId;
        this.username = username;
        this.password = password;
        this.registerDate = registerDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}