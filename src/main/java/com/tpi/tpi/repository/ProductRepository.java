package com.tpi.tpi.repository;

import com.tpi.tpi.model.ProductCategory;
import com.tpi.tpi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
        String sql = "SELECT p.product_id, p.name, p.description, p.unit_price, p.stock, c.category_id, c.name as category_name " +
                     "FROM Products p " +
                     "JOIN Prod_categories c ON p.category_id = c.category_id";

        return jdbcTemplate.query(sql, this::mapRowToProducto);
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE Products SET name = ?, description = ?, unit_price = ?, stock = ?, category_id = ? WHERE product_id = ?";
        jdbcTemplate.update(sql, product.getNombre(), product.getDescripcion(), product.getPrecioUnitario(), product.getStock(), product.getCategoria().getIdCategoria(), product.getIdProducto());
    }

    private Product mapRowToProducto(ResultSet rs, int rowNum) throws SQLException {
        ProductCategory categoria = new ProductCategory(
                rs.getInt("category_id"),
                rs.getString("category_name")
        );
        return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                categoria
        );
    }
}
