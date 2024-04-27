package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.response.ReceiptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceiptService {
    Page<ReceiptResponse> findAll(Pageable pageable);

    Page<ReceiptResponse> findByEmployeeNameOrSupplierNameContaining(String employeeName, String supplierName, Pageable pageable);

    long count();

    ReceiptResponse findByReceiptId(Long id);

    ReceiptResponse createReceipt(ReceiptRequest receiptRequest);
}
