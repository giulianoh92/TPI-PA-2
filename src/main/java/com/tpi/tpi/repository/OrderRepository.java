package com.tpi.tpi.repository;

import com.tpi.tpi.model.ProductCategory;
import com.tpi.tpi.model.Status;
import com.tpi.tpi.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
        String sql = "SELECT o.*, s.*, c.*, i.*, p.*, pc.*  FROM Orders o " +
                "JOIN Status s ON o.status_id = s.status_id " +
                "JOIN Customer c ON o.customer_id = c.customer_id " +
                "JOIN Item i ON o.order_id = i.order_id" +
                "JOIN Product p ON i.product_id = p.product_id" +
                "JOIN ProductCategory pc ON p.category_id = pc.category_id";

        return jdbcTemplate.query(sql, this::mapRowToOrder);
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        Status status = new Status(
                rs.getInt("status_id"),
                rs.getString("status_name")
        );
        ProductCategory productCategory = new ProductCategory(
                rs.getInt("category_id"),
                rs.getString("category_name")
        );
        Product product = new Product(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getString("product_description"),
                rs.getDouble("product_price"),
                rs.getInt("product_stock"),
                productCategory
        );
        List<Item> items = new ArrayList<>();
        items.add(new Item(
                rs.getInt("item_id"),
                rs.getInt("amount"),
                product
        ));
        Order order = new Order(
                rs.getInt("order_id"),
                status,
                items
        );
        return order;
    }
}
