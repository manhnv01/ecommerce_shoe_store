package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {
}