package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.CartDetailsRequest;
import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.response.CartDetailsResponse;
import com.nvm.shoestoreapi.dto.response.CartResponse;
import com.nvm.shoestoreapi.entity.Cart;
import com.nvm.shoestoreapi.entity.CartDetails;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;

import java.util.List;

public interface CartService {
    CartResponse getCartByUserEmail(String email);

    CartDetailsResponse addProductToCart(CartDetailsRequest cartDetailsRequest);

    void deleteCartDetailsById(Long id);

    CartDetailsResponse updateProductQuantity(CartDetailsRequest cartDetailsRequest);
}
