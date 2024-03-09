package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.ProductColor;
import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WishlistService {
    Page<Product> findAll(Pageable pageable);
    Wishlist add (ProductRequest productRequest);
    long count();
    Optional<Wishlist> findById(Long id);
    void deleteById(Long id);
}
