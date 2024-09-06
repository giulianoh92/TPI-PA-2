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
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all products", e);
        }
    }

    public void updateProduct(Product product) {
        try {
            productRepository.updateProduct(product);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating product", e);
        }
    }

    public List<ProductCategory> getAllCategories() {
        try {
            return productRepository.findAllCategories();
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all categories", e);
        }
    }
}