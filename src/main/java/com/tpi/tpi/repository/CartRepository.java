package com.tpi.tpi.repository;

import com.tpi.tpi.model.Cart;
import com.tpi.tpi.model.Item;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.ProductCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CartRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to find all carts
    public List<Cart> findAll() {
        String sql = "SELECT * FROM Carts";
        return jdbcTemplate.query(sql, this::mapRowToCart);
    }

    // Method to find a cart by its ID
    public Cart findById(int cartId) {
        String sql = "SELECT * FROM Carts WHERE cart_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToCart, cartId);
    }

    // Method to map a row from the ResultSet to a Cart object
    private Cart mapRowToCart(ResultSet rs, int rowNum) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        String sql = "SELECT * FROM Items WHERE cart_id = ?";
        List<Item> items = jdbcTemplate.query(sql, this::mapRowToItem, cart.getCartId());
        items.forEach(cart::addItem);
        return cart;
    }

    // Method to map a row from the ResultSet to an Item object
    private Item mapRowToItem(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        Product product = jdbcTemplate.queryForObject(sql, this::mapRowToProduct, rs.getInt("product_id"));
        return new Item(
            rs.getInt("amount"),
            product
        );
    }

    // Method to map a row from the ResultSet to a Product object
    private Product mapRowToProduct(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM Prod_categories WHERE category_id = ?";
        ProductCategory category = jdbcTemplate.queryForObject(sql, this::mapRowToProductCategory, rs.getInt("category_id"));
        return new Product(
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getFloat("unit_price"),
                rs.getInt("stock"),
                category
        );
    }

    // Method to map a row from the ResultSet to a ProductCategory object
    private ProductCategory mapRowToProductCategory(ResultSet rs, int rowNum) throws SQLException {
        return new ProductCategory(
            rs.getInt("category_id"),
            rs.getString("name")
        );
    }
}