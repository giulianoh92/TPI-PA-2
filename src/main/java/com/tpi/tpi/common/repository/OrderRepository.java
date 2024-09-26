package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.ProductCategory;
import com.tpi.tpi.common.model.Status;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Order;
import com.tpi.tpi.common.model.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository for managing Order entities.
 */
@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = Logger.getLogger(OrderRepository.class.getName());

    public List<Order> findAll() {
        String sql = """
                SELECT o.*, s.*, py.*, pm.* FROM Orders o \
                JOIN Statuses s ON o.status_id = s.status_id \
                JOIN Payments py ON o.payment_id = py.payment_id \
                JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id \
                ORDER BY o.order_id DESC \
                """;
        try {
            List<Order> orders = jdbcTemplate.query(sql, this::mapRowToOrder);

            for (Order order : orders) {
                if (order != null) {
                    order.addItemList(findItemsByOrderId(order.getOrderId()));
                }
            }

            return orders;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all orders", e);
            throw new RuntimeException("Error fetching all orders", e);
        }
    }

    public List<Order> findByUserId(int userId) {
        String sql = """
                SELECT o.*, s.*, py.*, pm.* FROM Orders o \
                JOIN Statuses s ON o.status_id = s.status_id \
                JOIN Payments py ON o.payment_id = py.payment_id \
                JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id \
                WHERE o.customer_id = ? \
                ORDER BY o.order_id DESC \
                """;
        try {
            List<Order> orders = jdbcTemplate.query(sql, this::mapRowToOrder, userId);

            for (Order order : orders) {
                order.addItemList(findItemsByOrderId(order.getOrderId()));
            }

            return orders;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching orders by user ID", e);
            throw new RuntimeException("Error fetching orders by user ID", e);
        }
    }

    public List<Status> findAllStatuses() {
        String sql = "SELECT * FROM Statuses";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Status(
                    rs.getInt("status_id"),
                    rs.getString("name")
            ));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all statuses", e);
            throw new RuntimeException("Error fetching all statuses", e);
        }
    }

    public Order findOrderById(int orderId) {
        String sql = """
                SELECT o.*, s.*, py.*, pm.* FROM Orders o \
                JOIN Statuses s ON o.status_id = s.status_id \
                JOIN Payments py ON o.payment_id = py.payment_id \
                JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id \
                WHERE o.order_id = ? LIMIT 1\
                """;
        try {
            Order order = jdbcTemplate.queryForObject(sql, this::mapRowToOrder, orderId);
        
            if (order != null) {
                order.addItemList(findItemsByOrderId(order.getOrderId()));
            }
        
            return order;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching order by ID", e);
            throw new RuntimeException("Error fetching order by ID", e);
        }
    }

    public void updateOrder(Order order) {
        String sql = "UPDATE Orders SET status_id = ? WHERE order_id = ?";
        try {
            jdbcTemplate.update(sql, order.getStatus().getStatusId(), order.getOrderId());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order", e);
            throw new RuntimeException("Error updating order", e);
        }
    }

    public List<Payment> findAllPaymentMethods() {
        String sql = "SELECT * FROM Payment_methods";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> new Payment(
                    rs.getInt("payment_met_id"),
                    null,
                    rs.getString("name"),
                    0
            ));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all payment methods", e);
            throw new RuntimeException("Error fetching all payment methods", e);
        }
    }
    
    public void addOrder(Order order, int userId) {
        String sql = "INSERT INTO Orders (status_id, payment_id, customer_id) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sql, order.getStatus().getStatusId(), order.getPayment().getPaymentId(), userId);
            Integer orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            if (orderId != null) {
                order.setOrderId(orderId);
            } else {
                throw new RuntimeException("Failed to retrieve the last inserted order ID");
            }
    
            for (Item item : order.getItems()) {
                String itemSql = "INSERT INTO Items (order_id, product_id, amount) VALUES (?, ?, ?)";
                jdbcTemplate.update(itemSql, orderId, item.getProduct().getProductId(), item.getAmount());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding order", e);
            throw new RuntimeException("Error adding order", e);
        }
    }

    public List<Item> findItemsByOrderId(int orderId) {
        String sql = """
                SELECT i.*, p.*, pc.* FROM Items i \
                JOIN Products p ON i.product_id = p.product_id \
                JOIN Prod_categories pc ON p.category_id = pc.category_id \
                WHERE i.order_id = ?\
                """;
        try {
            return jdbcTemplate.query(sql, this::mapRowToItem, orderId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching items by order ID", e);
            throw new RuntimeException("Error fetching items by order ID", e);
        }
    }

    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        Status status = new Status(
                rs.getInt("s.status_id"),
                rs.getString("s.name")
        );
        Payment payment = new Payment(
                rs.getInt("py.payment_id"),
                rs.getDate("py.date"),
                rs.getString("pm.name"),
                rs.getDouble("py.amount")
        );
        return new Order(
                rs.getInt("o.order_id"),
                status,
                payment
        );
    }

    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
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
                true,
                productCategory
        );
        return new Item(
                rs.getInt("i.amount"),
                product
        );
    }
}