package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.dto.response.ReceiptResponse;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReceiptService {
    Page<ReceiptResponse> findAll(Pageable pageable);
    Page<ReceiptResponse> findByEmployeeNameOrSupplierNameContaining(String employeeName, String supplierName,Pageable pageable);
    long count();
    ReceiptResponse findByReceiptId(Long id);
    ReceiptResponse createReceipt(ReceiptRequest receiptRequest);
}
