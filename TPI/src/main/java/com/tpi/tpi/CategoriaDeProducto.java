/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;

/**
 *
 * @author giu
 */
public class CategoriaDeProducto {
    private int idCategoria;
    private String nombre;
    
    public CategoriaDeProducto(int idCategoria, String nombre){
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
