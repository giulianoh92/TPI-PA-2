/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
//import java.io.Serializable;
import java.sql.Date;



/**
 *
 * @author anton
 */
public class Usuario /*implements Serializable*/{
    
    private int idUsuario;
    private String password; 
    private String emailUsuario;
    private String nombreUsuario;
    private Date fecha_registro;
    private int rol; //No me acuerdo para que era
     
    
     
    public Usuario(int id, String password_, String email, String nombre, Date fecha ) {
        this.idUsuario = id;
        this.password = password_;
        this.emailUsuario= email;
        this.nombreUsuario = nombre;
        this.fecha_registro = fecha;
        this.rol= 1; //ver que onda 
       
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

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    
    
}

 