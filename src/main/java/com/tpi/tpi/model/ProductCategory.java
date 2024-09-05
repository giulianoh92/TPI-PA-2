package com.tpi.tpi.model;

/**
 * Represents a product category.
 */
public class ProductCategory {
    private int idCategoria;
    private String nombre;

    public ProductCategory(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

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

    @Override
    public String toString() {
        return nombre; // Return only the name for display in JComboBox
    }
}