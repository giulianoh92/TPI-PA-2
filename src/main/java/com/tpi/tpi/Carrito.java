/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
import java.sql.Date;
/**
 *
 * @author giu
 */
public class Carrito {
    private Date fechaCreacion;

    Carrito(Date fechaCreacion){
        this.fechaCreacion = fechaCreacion;
    }

    //getters y setters
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
