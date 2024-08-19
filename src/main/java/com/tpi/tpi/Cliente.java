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

    public Cliente(String direccion, int id, String password_, String email, String nombre, Date fecha, int rol) {
        super(id, password_, email, nombre, fecha, rol);
        this.direccion = direccion;
    }
   

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }    
    
}