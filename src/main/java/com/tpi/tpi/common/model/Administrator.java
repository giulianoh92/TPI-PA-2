package com.tpi.tpi.common.model;
import java.sql.Date;

/**
 * Represents an administrator in the system.
 */
public class Administrator extends User {
    public Administrator(int UserId, String password, String username, Date registerDate) {
        super(UserId, password, username, registerDate);
    }
}