package com.tpi.tpi.controller;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.view.ProductView;

import java.util.List;

public class ProductController {
    private ProductService productService;
    private ProductView view;

    public ProductController(ProductService productService, ProductView view) {
        this.productService = productService;
        this.view = view;
    }
}
