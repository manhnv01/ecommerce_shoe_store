package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ChangePasswordRequest;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface OrderService {
    OrderResponse findById(Long id);

    OrderResponse create(OrderRequest orderRequest);

    OrderResponse update(Long id, Integer orderStatus, String cancelReason);

    OrderResponse updatePaymentStatus(Long id, Boolean paymentStatus, Date paymentTime);

    // lấy tất cả đơn hàng của 1 customer theo email
    Page<OrderResponse> findByCustomerAccountEmail(String email, Pageable pageable);
    // lọc theo orderStatus
    Page<OrderResponse> findByCustomerAccountEmailAndOrderStatus(String email, Integer orderStatus, Pageable pageable);
    // đếm theo orderStatus
    long countByCustomerAccountEmailAndOrderStatus(String email, Integer orderStatus);
    // đếm theo email
    long countByCustomerAccountEmail(String email);

    // admin
    Page<OrderResponse> findByOrderStatus(Integer orderStatus, Pageable pageable);
    Page<OrderResponse> findAll(Pageable pageable);
    long countByOrderStatus(Integer orderStatus);
    long count();
}
