package com.tpi.tpi.repository;

import com.tpi.tpi.model.ProductCategory;
import com.tpi.tpi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for managing Product entities.
 */
@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds all products.
     * @return a list of products.
     */
    public List<Product> findAll() {
        String sql = "SELECT p.product_id, p.name, p.description, p.unit_price, p.stock, c.category_id, c.name as category_name " +
                     "FROM Products p " +
                     "JOIN Prod_categories c ON p.category_id = c.category_id";
        return jdbcTemplate.query(sql, this::mapRowToProduct);
    }
    /**
     * Finds all Categories.
     * @return a list of categories.
     */
    public List<ProductCategory> findAllCategories() {
        String sql = "SELECT category_id, name FROM Prod_categories";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductCategory(rs.getInt("category_id"), rs.getString("name")));
    }
    /**
     * Updates a product.
     * @param product the product to update.
     */
    public void updateProduct(Product product) {
        String sql = "UPDATE Products SET name = ?, description = ?, unit_price = ?, stock = ?, category_id = ? WHERE product_id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getDescription(), product.getUnitPrice(), product.getStock(), product.getCategory().getCategoryId(), product.getProductId());
    }

    /**
     * Maps a row from the ResultSet to a Product object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return a Product object.
     * @throws SQLException if a database access error occurs.
     */
    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        ProductCategory category = new ProductCategory(
                rs.getInt("category_id"),
                rs.getString("category_name")
        );
        return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                category
        );
    }
}