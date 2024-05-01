package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.dto.response.ReceiptResponse;
import com.nvm.shoestoreapi.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    boolean existsBySupplierId(Long id);
    Page<Receipt> findByEmployeeNameContainingOrSupplierNameContaining(String employeeName, String supplierName,Pageable pageable);
    List<Receipt> findAllByCreatedAtBetween(Date from, Date to);
}
