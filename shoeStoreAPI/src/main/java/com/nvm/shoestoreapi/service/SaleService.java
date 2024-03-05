package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.SaleRequest;
import com.nvm.shoestoreapi.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SaleService {
    List<Sale> findAll();

    Page<Sale> findAll(Pageable pageable);

    Sale create(SaleRequest request);

    Sale update(SaleRequest request);

    void deleteById(Long id);

    Page<Sale> findByNameContaining(String name, Pageable pageable);

    long count();

    Optional<Sale> findById(Long id);
}
