package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByPhone(String phone);
}
