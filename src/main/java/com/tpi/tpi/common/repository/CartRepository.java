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

    /**
     * Finds all carts.
     * @return a list of carts.
     */
    public List<Cart> findAll() {
        String sql = "SELECT * FROM Carts";
        return jdbcTemplate.query(sql, this::mapRowToCart);
    }

    /**
     * Finds a cart by its ID.
     * @param cartId the cart ID.
     * @return the cart.
     */
    public Cart findById(int cartId) {
        String sql = "SELECT * FROM Carts WHERE cart_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToCart, cartId);
    }

    /**
     * Maps a row from the ResultSet to a Cart object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return a Cart object.
     * @throws SQLException if a database access error occurs.
     */
    private Cart mapRowToCart(ResultSet rs, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        String sql = "SELECT * FROM Items WHERE cart_id = ?";
        List<Item> items = jdbcTemplate.query(sql, this::mapRowToItem, cart.getCartId());
        items.forEach(cart::addItem);
        return cart;
    }

    /**
     * Maps a row from the ResultSet to an Item object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return an Item object.
     * @throws SQLException if a database access error occurs.
     */
    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        Product product = jdbcTemplate.queryForObject(sql, this::mapRowToProduct, rs.getInt("product_id"));
        return new Item(
            rs.getInt("amount"),
            product
        );
    }

    /**
     * Maps a row from the ResultSet to a Product object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return a Product object.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Maps a row from the ResultSet to a ProductCategory object.
     * @param rs the ResultSet.
     * @param rowNum the row number.
     * @return a ProductCategory object.
     * @throws SQLException if a database access error occurs.
     */
    private ProductCategory mapRowToProductCategory(ResultSet rs, int rowNum) throws SQLException {
        return new ProductCategory(
            rs.getInt("category_id"),
            rs.getString("name")
        );
    }
}