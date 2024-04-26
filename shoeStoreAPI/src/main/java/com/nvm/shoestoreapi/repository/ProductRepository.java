package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.dto.response.ProductBestSellerResponse;
import com.nvm.shoestoreapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
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

    // lay danh sach san pham có enabled =true và có ngày hiện tịa nằm trong khoảng ngày bắt ầu và ngày kết thúc cua khuyen mai
    @Query("SELECT p FROM Product p JOIN p.sales s WHERE p.enabled = true AND CURRENT_TIMESTAMP() BETWEEN s.startDate AND s.endDate")
    List<Product> findEnabledProductsInSale();

    // lọc
    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    List<Product> findByNameContainingAndEnabledIsTrue(String name);

    // nhận vào 2 tham s tháng và năm ấy ra top 5 sản phẩm bán chạy nhất
    @Query("SELECT NEW com.nvm.shoestoreapi.dto.response.ProductBestSellerResponse(p.id, p.name, p.slug, p.thumbnail, SUM(od.quantity)) " +
            "FROM Product p " +
            "JOIN p.productColors pc " +
            "JOIN pc.productDetails pd " +
            "JOIN pd.orderDetails od " +
            "JOIN od.order o " +
            "WHERE o.orderStatus = 3 " +
            "AND MONTH(o.createdDate) = :month " +
            "AND YEAR(o.createdDate) = :year " +
            "GROUP BY p.id " +
            "ORDER BY SUM(od.quantity) DESC ")
    List<ProductBestSellerResponse> findProductsByOrderStatusAndDate(int month, int year, Pageable pageable);
}
