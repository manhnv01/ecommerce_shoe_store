package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ChangePasswordRequest;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.entity.Account;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface OrderService {
//    PaginationDTO<OrderResponse> findByOrderCustomerFullname(String fullname, Pageable pageable);
//
//    PaginationDTO<OrderResponse> findByCustomerAccountEmail(Long id, Pageable pageable);

    OrderResponse findById(Long id);

    OrderResponse create(OrderRequest orderRequest);

    OrderResponse update(Long id, Integer orderStatus, String cancelReason);

    OrderResponse updatePaymentStatus(Long id, Boolean paymentStatus, Date paymentTime);
}
