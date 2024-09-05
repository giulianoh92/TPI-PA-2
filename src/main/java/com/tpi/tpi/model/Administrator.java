package com.tpi.tpi.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Represents an administrator in the system.
 */
public class Administrator extends User implements Serializable {
    public Administrator(int idUsuario, String password, String nombreUsuario, Date fechaRegistro) {
        super(idUsuario, password, nombreUsuario, fechaRegistro);
    }
}