package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    boolean existsBySupplierId(Long id);
}
