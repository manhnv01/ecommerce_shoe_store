package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Sale;
import com.nvm.shoestoreapi.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    boolean existsByName(String name);
    Page<Sale> findByNameContaining(String name, Pageable pageable);
    List<Sale> findByStartDateBetweenOrEndDateBetween(Date startDate1, Date endDate1, Date startDate2, Date endDate2);
}