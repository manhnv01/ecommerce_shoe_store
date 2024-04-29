package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.dto.response.CostResponse;
import com.nvm.shoestoreapi.dto.response.CostReturnResponse;
import com.nvm.shoestoreapi.dto.response.ProductBestSellerResponse;
import com.nvm.shoestoreapi.dto.response.RevenueResponse;
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
            "AND MONTH(o.completedDate) = :month " +
            "AND YEAR(o.completedDate) = :year " +
            "GROUP BY p.id " +
            "ORDER BY SUM(od.quantity) DESC ")
    List<ProductBestSellerResponse> findProductsByOrderStatusAndDate(int month, int year, Pageable pageable);

    // thống kê doanh thu và chi phí theo cac thang, gom nhóm theo thang
    @Query("SELECT NEW com.nvm.shoestoreapi.dto.response.RevenueResponse(MONTH(o.completedDate), SUM(od.quantity * od.price)) " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "WHERE o.orderStatus = 3 " +
            "AND YEAR(o.completedDate) = :year " +
            "GROUP BY MONTH(o.completedDate) " +
            "ORDER BY MONTH(o.completedDate) ASC")
    List<RevenueResponse> findRevenueByYear(int year);

    // thống kê doanh thu và chi phí theo cac thang, gom nhóm theo thang
    @Query("SELECT NEW com.nvm.shoestoreapi.dto.response.CostResponse(MONTH(r.createdAt), SUM(rd.quantity * rd.price)) " +
            "FROM Receipt r " +
            "JOIN r.receiptDetails rd " +
            "WHERE YEAR(r.createdAt) = :year " +
            "GROUP BY MONTH(r.createdAt) " +
            "ORDER BY MONTH(r.createdAt) ASC")
    List<CostResponse> findCostByYear(int year);

    @Query("SELECT NEW com.nvm.shoestoreapi.dto.response.CostReturnResponse(MONTH(rp.createdAt), " +
            "SUM((rpd.quantity * od.price) + o.total_fee))" +
            "FROM ReturnProduct rp " +
            "JOIN rp.returnProductDetails rpd " +
            "JOIN rp.order o " +
            "JOIN o.orderDetails od " +
            "WHERE YEAR(rp.createdAt) = :year AND rpd.returnType = true AND od.productDetails.id = rpd.productDetails.id " +
            "GROUP BY MONTH(rp.createdAt) " +
            "ORDER BY MONTH(rp.createdAt) ASC")
    List<CostReturnResponse> findCostReturnByYear(int year);

}
