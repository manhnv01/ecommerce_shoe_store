package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.ProductColor;
import com.nvm.shoestoreapi.entity.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponse create(ProductRequest productRequest);
    ProductResponse update(ProductRequest productRequest);
    Page<ProductResponse> getAll(Pageable pageable);
    Page<ProductResponse> search(String name, Pageable pageable);
    Page<ProductResponse> searchByStatus(boolean enabled, Pageable pageable);
    Page<ProductResponse> searchByNameAndStatus(String name, boolean enabled, Pageable pageable);
    Optional<ProductResponse> getById(Long id);

    long count();
    long countByEnabledTrue();
    long countByEnabledFalse();
    void updatesStatus(List<Long> categoryIds, boolean enabled);
    void deleteProductColor(Long id);
    void changeSwitch (Long id);
    void deleteImageById(Long id, String imageName);
    void deleteById(Long id);

    Page<ProductResponse> getProductsByTotalQuantity(Pageable pageable, boolean isZeroQuantity);

    List<ProductResponse> findAll();
    ProductDetails findByProductDetailsId(Long id);

    Page<ProductResponse> findAllByEnabledIsTrue(Pageable pageable);
    ProductResponse findBySlug(String slug);

    List<ProductResponse> findTop10ByCategory_IdAndBrand_IdAndEnabledIsTrue(Long categoryId, Long brandId);

    List<ProductResponse> findTop10ByEnabledIsTrueOrderByCreatedAtDesc();
}
