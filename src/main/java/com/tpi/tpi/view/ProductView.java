package com.tpi.tpi.view;

import com.tpi.tpi.model.ProductCategory;

/**
 * Class responsible for displaying product details.
 */
public class ProductView {
    /**
     * Prints product details to the console.
     * 
     * @param idProducto Product ID
     * @param nombre Product name
     * @param descripcion Product description
     * @param precioUnitario Product unit price
     * @param stock Product stock quantity
     * @param categoria Product category
     */
    public void printProductDetails(int idProducto, String nombre, String descripcion, float precioUnitario, int stock, ProductCategory categoria){
        System.out.println("Producto ID: " + idProducto +
                           ", Nombre: " + nombre +
                           ", Desc: " + descripcion +
                           ", P/U: " + precioUnitario +
                           ", Stock: " + stock +
                           ", Cat: " + categoria.getNombre());
    }
}