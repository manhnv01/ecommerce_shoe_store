package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.ProductColor;
import com.nvm.shoestoreapi.entity.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByEnabled(boolean enabled, Pageable pageable);
    Product createProduct (ProductRequest productRequest);
    Product updateProduct (ProductRequest productRequest);
    long count();
    long countByEnabledTrue();
    long countByEnabledFalse();
    Optional<Product> findById(Long id);
    void updatesStatus(List<Long> categoryIds, boolean enabled);
    void deleteProductColor(Long id);
    void changeSwitch (Long id);
    void deleteImageById(Long id, String imageName);
    void deleteById(Long id);
    List<ProductColor> findByProductId(Long id);
    List<ProductDetails> findByProductColorId(Long id);
    ProductDetails findByProductDetailsId(Long id);
}
