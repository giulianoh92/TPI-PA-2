package com.tpi.tpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Producto> findAll() {
        String sql = "SELECT p.product_id, p.name, p.description, p.unit_price, p.stock, c.category_id, c.name as category_name " +
                     "FROM Products p " +
                     "JOIN Prod_categories c ON p.category_id = c.category_id";

        return jdbcTemplate.query(sql, this::mapRowToProducto);
    }

    private Producto mapRowToProducto(ResultSet rs, int rowNum) throws SQLException {
        CategoriaDeProducto categoria = new CategoriaDeProducto(
                rs.getInt("category_id"),
                rs.getString("category_name")
        );
        return new Producto(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                categoria
        );
    }
}
