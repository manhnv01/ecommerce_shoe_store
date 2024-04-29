package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.dto.response.ReturnProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReturnProductService {
    Page<ReturnProductResponse> findAll(Pageable pageable);

    ReturnProductResponse create(ReturnProductRequest ReturnProductRequest);

    ReturnProductResponse update(ReturnProductRequest ReturnProductRequest);

    void deleteById(Long id);

    long count();

    Optional<ReturnProductResponse> findById(Long id);

    int countByStatus(boolean status);

    Page<ReturnProductResponse> findByStatus(boolean status, Pageable pageable);

    Page<ReturnProductResponse> findByEmployeeNameOrCustomerNameContaining(String employeeName, String customerName, Pageable pageable);
}
