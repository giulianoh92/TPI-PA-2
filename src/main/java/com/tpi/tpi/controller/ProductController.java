package com.tpi.tpi.controller;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.view.ProductView;
import java.util.List;

/**
 * Controlador para manejar la lógica de negocios de productos.
 */
public class ProductController {
    private ProductView view;

    public ProductController(ProductView view) {
        this.view = view;
    }

    public void updateView(List<Product> products) {
        for (Product product : products) {
            view.printProductDetails(product.getIdProducto(), product.getNombre(), product.getDescripcion(), product.getPrecioUnitario(), product.getStock(), product.getCategoria());
        }
    }
}
