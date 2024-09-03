package com.tpi.tpi.repository;

import com.tpi.tpi.model.ProductCategory;
import com.tpi.tpi.model.Status;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.Item;
import com.tpi.tpi.model.Order;
import com.tpi.tpi.model.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Order> findAll() {
        String sql = "SELECT o.*, s.*, i.*, p.*, pc.*, py.*, pm.*  FROM Orders o " +
                "JOIN Statuses s ON o.status_id = s.status_id " +
                "JOIN Items i ON o.order_id = i.order_id " +
                "JOIN Products p ON i.product_id = p.product_id " +
                "JOIN Prod_categories pc ON p.category_id = pc.category_id " +
                "JOIN Payments py ON o.payment_id = py.payment_id " +
                "JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id";

        return jdbcTemplate.query(sql, this::mapRowToOrder);
    }

    public List<Order> findByUserId(int id) {
        String sql = "SELECT o.*, s.*, i.*, p.*, pc.*, py.*, pm.*  FROM Orders o " +
                "JOIN Statuses s ON o.status_id = s.status_id " +
                "JOIN Items i ON o.order_id = i.order_id " +
                "JOIN Products p ON i.product_id = p.product_id " +
                "JOIN Prod_categories pc ON p.category_id = pc.category_id " +
                "JOIN Payments py ON o.payment_id = py.payment_id " +
                "JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id" +
                "WHERE o.user_id = ?";

        return jdbcTemplate.query(sql, this::mapRowToOrder);
    }

    public Order findOrderById(int id){
        String sql = "SELECT o.*, s.*, i.*, p.*, pc.*, py.*, pm.*  FROM Orders o " +
                "JOIN Statuses s ON o.status_id = s.status_id " +
                "JOIN Items i ON o.order_id = i.order_id " +
                "JOIN Products p ON i.product_id = p.product_id " +
                "JOIN Prod_categories pc ON p.category_id = pc.category_id " +
                "JOIN Payments py ON o.payment_id = py.payment_id " +
                "JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id" +
                "WHERE o.order_id = ? LIMIT 1";

        return jdbcTemplate.queryForObject(sql, this::mapRowToOrder, id);
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        Status status = new Status(
                rs.getInt("s.status_id"),
                rs.getString("s.name")
        );
        ProductCategory productCategory = new ProductCategory(
                rs.getInt("pc.category_id"),
                rs.getString("pc.name")
        );
        Product product = new Product(
                rs.getInt("p.product_id"),
                rs.getString("p.name"),
                rs.getString("p.description"),
                rs.getFloat("p.unit_price"),
                rs.getInt("p.stock"),
                productCategory
        );
        List<Item> items = new ArrayList<>();
        items.add(new Item(
                rs.getInt("i.amount"),
                product
        ));
        Payment payment = new Payment(
                rs.getInt("py.payment_id"),
                rs.getDate("py.date"),
                rs.getString("pm.name"),
                rs.getDouble("py.amount")
        );
        Order order = new Order(
                rs.getInt("o.order_id"),
                status,
                payment
        );
        return order;
    }
}
