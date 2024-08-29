/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi.model;

/**
 *
 * @author giu
 */
public class ProductCategory { //queda por ver si es necesaria esta clase
    private int idCategoria;
    private String nombre;
    
    public ProductCategory(int idCategoria, String nombre){
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    //getters y setters
    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
