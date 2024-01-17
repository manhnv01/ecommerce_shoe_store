package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.entity.Cart;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;

import java.util.List;

public interface CartService {
    List<Cart> getAllProductInCart();
    Cart addProductToCart (CartRequest cartRequest);
    Cart updateQuantityProductInCart (Long id, CartRequest cartRequest);
    void deleteProductInCartById(Long id);
}
