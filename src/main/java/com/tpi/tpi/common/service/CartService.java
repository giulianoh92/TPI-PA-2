package com.tpi.tpi.common.service;

import com.tpi.tpi.common.model.Cart;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart getCartById(int cartId) {
        return cartRepository.findById(cartId);
    }

    public Cart getCartByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addItemToCart(Item item, int cartId) {
        cartRepository.addItem(item, cartId);
    }

    public void removeItemFromCart(Item item, int cartId) {
        cartRepository.removeItem(item, cartId);
    }

    public void updateCart(Cart cart) {
        cartRepository.updateCart(cart);
    }
}
