package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByEnabled(boolean enabled, Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByNameContainingAndEnabled(String name, boolean enabled, Pageable pageable);
    long countByEnabledTrue();
    long countByEnabledFalse();
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Page<Product> findByEnabledIsTrue(Pageable pageable);
    Page<Product> findByEnabledIsTrueAndBrand_Slug(String slug, Pageable pageable);
    Optional<Product> findBySlug(String slug);

    // client
    List<Product> findTop10ByCategory_IdAndBrand_IdAndEnabledIsTrue(Long categoryId, Long brandId);

    // lấy 10 sản phẩm mới nhất
    List<Product> findTop10ByEnabledIsTrueOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productColors pc JOIN pc.productDetails pd GROUP BY p HAVING SUM(pd.quantity) = 0")
    Page<Product> findProductsWithTotalQuantityZero(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.productColors pc JOIN pc.productDetails pd GROUP BY p HAVING SUM(pd.quantity) > 0")
    Page<Product> findProductsWithTotalQuantityNotZero(Pageable pageable);
}
