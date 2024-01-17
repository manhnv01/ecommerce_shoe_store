package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();
    Product createProduct (ProductRequest productRequest);
    Product updateProduct (Long id, ProductRequest productRequest);
    void deleteProductById(Long id);
}
