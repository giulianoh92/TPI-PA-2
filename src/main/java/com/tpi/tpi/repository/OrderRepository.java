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
import java.util.List;

/**
 * Repository for managing Order entities.
 */
@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Finds all orders.
     * @return a list of orders.
     */
    public List<Order> findAll() {
        String sql = "SELECT o.*, s.*, py.*, pm.* FROM Orders o " +
                "JOIN Statuses s ON o.status_id = s.status_id " +
                "JOIN Payments py ON o.payment_id = py.payment_id " +
                "JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id";
        List<Order> orders = jdbcTemplate.query(sql, this::mapRowToOrder);

        // Fetch items for each order
        for (Order order : orders) {
            List<Item> items = findItemsByOrderId(order.getOrderId());
            order.addItemList(items);
        }

        return orders;
    }

    /**
     * Finds orders by user ID.
     * @param userId the user ID.
     * @return a list of orders.
     */
    public List<Order> findByUserId(int userId) {
        String sql = "SELECT o.*, s.*, py.*, pm.* FROM Orders o " +
                "JOIN Statuses s ON o.status_id = s.status_id " +
                "JOIN Payments py ON o.payment_id = py.payment_id " +
                "JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id " +
                "WHERE o.user_id = ?";
        List<Order> orders = jdbcTemplate.query(sql, this::mapRowToOrder, userId);

        // Fetch items for each order
        for (Order order : orders) {
            List<Item> items = findItemsByOrderId(order.getOrderId());
            order.addItemList(items);
        }

        return orders;
    }

    /**
     * Finds all statuses.
     * @return a list of statuses.
     */
    public List<Status> findAllStatuses() {
        String sql = "SELECT * FROM Statuses";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Status(
                rs.getInt("status_id"),
                rs.getString("name")
        ));
    }

    /**
     * Finds an order by its ID.
     * @param orderId the order ID.
     * @return the order.
     */
    public Order findOrderById(int orderId) {
        String sql = "SELECT o.*, s.*, py.*, pm.* FROM Orders o " +
                "JOIN Statuses s ON o.status_id = s.status_id " +
                "JOIN Payments py ON o.payment_id = py.payment_id " +
                "JOIN Payment_methods pm ON py.payment_met_id = pm.payment_met_id " +
                "WHERE o.order_id = ? LIMIT 1";
        Order order = jdbcTemplate.queryForObject(sql, this::mapRowToOrder, orderId);

        // Fetch items for the order
        List<Item> items = findItemsByOrderId(order.getOrderId());
        order.addItemList(items);

        return order;
    }

    /**
     * Updates an order.
     * @param order the order to update.
     */
    public void updateOrder(Order order) {
        String sql = "UPDATE Orders SET status_id = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, order.getStatus().getUserId(), order.getOrderId());
    }

    /**
     * Finds items by order ID.
     * @param orderId the order ID.
     * @return a list of items.
     */
    private List<Item> findItemsByOrderId(int orderId) {
        String sql = "SELECT i.*, p.*, pc.* FROM Items i " +
                "JOIN Products p ON i.product_id = p.product_id " +
                "JOIN Prod_categories pc ON p.category_id = pc.category_id " +
                "WHERE i.order_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToItem, orderId);
    }

    /**
     * Maps a row from the ResultSet to an Order object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return an Order object.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Maps a row from the ResultSet to an Item object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return an Item object.
     * @throws SQLException if a database access error occurs.
     */
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
                productCategory
        );
        return new Item(
                rs.getInt("i.amount"),
                product
        );
    }
}