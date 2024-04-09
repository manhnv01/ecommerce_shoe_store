package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // lấy tất cả đơn hàng của 1 customer theo email
    Page<Order> findByCustomer_Account_Email(String email, Pageable pageable);

    // lọc theo orderStatus
    Page<Order> findByCustomer_Account_EmailAndOrderStatus(String email, Integer orderStatus, Pageable pageable);

    // đếm theo orderStatus và email
    long countByCustomer_Account_EmailAndOrderStatus(String email, Integer orderStatus);
    // đếm theo email
    long countByCustomer_Account_Email(String email);


    // admin
    Page<Order> findByOrderStatus(Integer orderStatus, Pageable pageable);
    long countByOrderStatus(Integer orderStatus);
}