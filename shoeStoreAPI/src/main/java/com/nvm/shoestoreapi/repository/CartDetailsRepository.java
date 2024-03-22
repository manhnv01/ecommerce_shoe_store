package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.CartDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {
    CartDetails findByCart_IdAndProductDetails_Id(Long cartId, Long productDetailsId);
    List<CartDetails> findByCart_IdOrderByCart_IdDesc(Long cartId);
    Optional<CartDetails> findByCartIdAndProductDetailsId(Long cartId, Long productDetailsId);
    int countByCart_Id(Long cartId);
}