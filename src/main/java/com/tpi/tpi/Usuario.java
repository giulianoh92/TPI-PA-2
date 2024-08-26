/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
import java.io.Serializable;
import java.sql.Date;



/**
 *
 * @author anton
 */
public class Usuario implements Serializable{
    
    protected int idUsuario;
    protected String password; 
    protected String nombreUsuario;
    protected Date fechaRegistro;
    // el rol lo define la clase
    
     
    public Usuario(int idUsuario, String password, String nombreUsuario, Date fechaRegistro) {
        this.idUsuario = idUsuario;
        this.password = password;
        this.nombreUsuario = nombreUsuario;
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

    public Date getfechaRegistro() {
        return fechaRegistro;
    }

    public void setfechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}

 