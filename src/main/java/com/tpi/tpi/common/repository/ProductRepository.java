package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.ProductCategory;
import com.tpi.tpi.common.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
        String sql = """
                     SELECT p.product_id, p.name, p.description, p.unit_price, p.stock, p.image_path, p.is_active, c.category_id, c.name as category_name
                     FROM Products p
                     JOIN Prod_categories c ON p.category_id = c.category_id
                     WHERE p.is_active = 1
                     ORDER BY p.product_id
                     """;
    
        List<Product> products = jdbcTemplate.query(sql, this::mapRowToProduct);
        return products;
    }
    
    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        ProductCategory category = new ProductCategory(
            rs.getInt("category_id"),
            rs.getString("category_name")
        );
        Product product = new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                rs.getBoolean("is_active"),
                category
        );
        product.setImagePath(rs.getString("image_path"));
        return product;
    }

    public void updateProduct(Product product) {
        if (product.getProductId() == 0) {
            addProduct(product);
            return;
        }
    
        String sql = "UPDATE Products SET name = ?, description = ?, unit_price = ?, stock = ?, category_id = ?, is_active = ?, image_path = ? WHERE product_id = ?";
        try {            
            // Ensure the image path is relative
            String relativeImagePath = product.getImagePath();
            if (relativeImagePath != null) {
                relativeImagePath = relativeImagePath.replace(new File("").getAbsolutePath() + "/", "");
                if (relativeImagePath.startsWith("src/main/resources/static/images/")) {
                    relativeImagePath = relativeImagePath.replace("src/main/resources/static/images/", "");
                }
            }
            
            jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getUnitPrice(), product.getStock(), product.getCategory().getCategoryId(), product.isActive(), relativeImagePath, product.getProductId());
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating product", e);
        }
    }

    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (name, description, unit_price, stock, category_id, is_active, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            // Log the product being added
            jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getUnitPrice(), product.getStock(), product.getCategory().getCategoryId(), product.isActive(), product.getImagePath());
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error adding product", e);
        }
    }


    public List<ProductCategory> findAllCategories() {
        String sql = "SELECT category_id, name FROM Prod_categories";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductCategory(rs.getInt("category_id"), rs.getString("name")));
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all categories", e);
        }
    }

}