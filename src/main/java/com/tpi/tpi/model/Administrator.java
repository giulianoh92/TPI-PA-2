package com.tpi.tpi.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Represents an administrator in the system.
 */
public class Administrator extends User implements Serializable {
    public Administrator(int UserId, String password, String username, Date registerDate) {
        super(UserId, password, username, registerDate);
    }
}