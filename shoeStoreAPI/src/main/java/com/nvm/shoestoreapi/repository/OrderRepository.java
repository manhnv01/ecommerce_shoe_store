package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}