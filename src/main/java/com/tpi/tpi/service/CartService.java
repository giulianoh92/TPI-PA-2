package com.tpi.tpi.service;

import com.tpi.tpi.model.Cart;
import com.tpi.tpi.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getAllProducts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(int cartId) {
        return cartRepository.findById(cartId);
    }
}
