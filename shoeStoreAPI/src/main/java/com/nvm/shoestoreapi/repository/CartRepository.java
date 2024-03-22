package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(Long customerId);
    Cart findByCustomerAccountEmail(String email);
}