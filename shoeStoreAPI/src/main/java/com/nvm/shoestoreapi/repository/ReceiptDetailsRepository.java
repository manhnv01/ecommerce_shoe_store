package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.entity.ReceiptDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDetailsRepository extends JpaRepository<ReceiptDetails,Long> {
    boolean existsByProductDetailsId(Long productDetailsId);
}
