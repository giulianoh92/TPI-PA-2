/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi.controller;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.view.ProductView;
import java.util.List;

/**
 *
 * @author giu
 */
public class ProductController {
    private Product model;
    private ProductView view;
    
    public ProductController(Product model, ProductView view){
        this.model = model;
        this.view = view;
    }
    
    public void updateView(){
        view.printProductDetails(model.getIdProducto(), model.getNombre(), model.getDescripcion(), model.getPrecioUnitario(), model.getStock(), model.getCategoria());
    }
}
