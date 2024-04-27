package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.dto.response.ReportBrandResponse;
import com.nvm.shoestoreapi.dto.response.ReportCategoryResponse;
import com.nvm.shoestoreapi.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    boolean existsByProductDetailsId(Long productDetailsId);
    OrderDetails findByOrderIdAndProductDetailsId(Long orderId, Long productDetailsId);
    long countByProductDetails_ProductColor_Product_Id(Long productId);

    @Query("SELECT NEW com.nvm.shoestoreapi.dto.response.ReportCategoryResponse(c.name, SUM(od.quantity)) " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.productDetails pd " +
            "JOIN pd.productColor pc " +
            "JOIN pc.product p " +
            "JOIN p.category c " +
            "WHERE o.orderStatus = 3 " +
            "GROUP BY c.name")
    List<ReportCategoryResponse> getProductCountByCategory();

    @Query("SELECT NEW com.nvm.shoestoreapi.dto.response.ReportBrandResponse(b.name, SUM(od.quantity)) " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.productDetails pd " +
            "JOIN pd.productColor pc " +
            "JOIN pc.product p " +
            "JOIN p.brand b " +
            "WHERE o.orderStatus = 3 " +
            "GROUP BY b.name")
    List<ReportBrandResponse> getProductCountByBrand();

}