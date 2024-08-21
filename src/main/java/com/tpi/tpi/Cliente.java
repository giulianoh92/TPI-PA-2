/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Entity;
import java.sql.Date;

/**
 *
 * @author anton
 */
public class Cliente extends Usuario implements Serializable {
    
    private String direccion;
    private String emailUsuario;
    private Carrito carrito = new Carrito();

    public Cliente(String direccion, int id, String password, String emailUsuario, String nombreUsuario, Date fechaRegistro) {
        super(id, password, nombreUsuario, fechaRegistro);
        this.direccion = direccion;
        this.emailUsuario = emailUsuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return emailUsuario;
    }

    public void setEmail(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void addToCarrito(Item item) {
        carrito.addItem(item);
    }
    
}