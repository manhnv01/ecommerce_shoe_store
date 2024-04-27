package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.dto.response.ReturnProductResponse;
import com.nvm.shoestoreapi.entity.ReturnProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReturnProductService {
    Page<ReturnProductResponse> findAll(Pageable pageable);
    ReturnProductResponse create (ReturnProductRequest ReturnProductRequest);
    ReturnProductResponse update (ReturnProductRequest ReturnProductRequest);
    void deleteById(Long id);
    long count();
    Optional<ReturnProductResponse> findById(Long id);
}
