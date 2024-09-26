package com.tpi.tpi.common.repository;

import com.tpi.tpi.common.model.Cart;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for managing Cart entities.
 */
@Repository
public class CartRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Cart findById(int cartId) {
        String sql = "SELECT * FROM Carts WHERE cart_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToCart, cartId);
    }

    public Cart findByUserId(int userId) {
        String sql = "SELECT * FROM Carts WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToCart, userId);
    }

    public void addItem(Item item, int cartId) {
        String sql = "INSERT INTO Items (cart_id, product_id, amount) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, cartId, item.getProduct().getProductId(), item.getAmount());
    }

    public void removeItem(Item item, int cartId) {
        String sql = "DELETE FROM Items WHERE cart_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, cartId, item.getProduct().getProductId());
    }

    public void updateItem(Item item, int cartId) {
        String sql = "UPDATE Items SET amount = ? WHERE cart_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, item.getAmount(), cartId, item.getProduct().getProductId());
    }

    public void updateCart(Cart cart) {
        String deleteSql = "DELETE FROM Items WHERE cart_id = ?";
        jdbcTemplate.update(deleteSql, cart.getCartId());

        String insertSql = "INSERT INTO Items (cart_id, product_id, amount) VALUES (?, ?, ?)";
        for (Item item : cart.getItems()) {
            jdbcTemplate.update(insertSql, cart.getCartId(), item.getProduct().getProductId(), item.getAmount());
        }
    }

    public void clearCart(int cartId) {
        String sql = "DELETE FROM Cart_items WHERE cart_id = ?";
        try {
            jdbcTemplate.update(sql, cartId);
        } catch (Exception e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    private Cart mapRowToCart(ResultSet rs, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        String sql = "SELECT * FROM Items WHERE cart_id = ?";
        List<Item> items = jdbcTemplate.query(sql, this::mapRowToItem, cart.getCartId());
        items.forEach(cart::addItem);
        return cart;
    }

    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        Product product = jdbcTemplate.queryForObject(sql, this::mapRowToProduct, rs.getInt("product_id"));
        return new Item(rs.getInt("amount"), product);
    }

    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Prod_categories WHERE category_id = ?";
        ProductCategory category = jdbcTemplate.queryForObject(sql, this::mapRowToProductCategory, rs.getInt("category_id"));
        return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                true,
                category
        );
    }

    private ProductCategory mapRowToProductCategory(ResultSet rs, int rowNum) throws SQLException {
        return new ProductCategory(rs.getInt("category_id"), rs.getString("name"));
    }
}