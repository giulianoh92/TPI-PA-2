package com.tpi.tpi.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Represents a user in the system.
 */
public class User implements Serializable {
    private int idUsuario;
    private String nombreUsuario;
    private String password;
    private Date fechaRegistro;

    public User(int idUsuario, String nombreUsuario, String password, Date fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}