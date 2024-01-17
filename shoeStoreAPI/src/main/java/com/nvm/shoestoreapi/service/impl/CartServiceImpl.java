package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.entity.Cart;
import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.repository.CartRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Cart> getAllProductInCart() {
        return null;
    }

    @Override
    public Cart addProductToCart(CartRequest cartRequest) {
        Optional<ProductDetails> existingProduct = productDetailsRepository.findById(cartRequest.getProductDetailsId());
        Cart cart = new Cart();
        modelMapper.map(cartRequest, Cart.class);
        if (existingProduct.isPresent()) {
            cart.setProductDetails(existingProduct.get());
            cart.setQuantity(cartRequest.getQuantity());
            return cartRepository.save(cart);
        } else
            throw new RuntimeException("Sản phẩm không tồn tại !");
    }

    @Override
    public Cart updateQuantityProductInCart(Long id, CartRequest cartRequest) {
        return null;
    }

    @Override
    public void deleteProductInCartById(Long id) {
        Optional<Cart> existingProduct = cartRepository.findById(id);
        if (existingProduct.isPresent()) {
            cartRepository.deleteById(id);
        } else
            throw new RuntimeException("Sản phẩm không tồn tại !");
    }
}
