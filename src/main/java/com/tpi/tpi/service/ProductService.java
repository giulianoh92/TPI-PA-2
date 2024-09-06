package com.tpi.tpi.service;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.ProductCategory;
import com.tpi.tpi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void updateProduct(Product product) {
        productRepository.updateProduct(product);
    }

    public List<ProductCategory> getAllCategories() {
        return productRepository.findAllCategories();
    }
}
