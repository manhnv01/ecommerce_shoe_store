package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsByPhone(String phone);
    Customer findByAccount_Email(String email);
    Customer findByPhone(String phone);
    Page<Customer> findByNameContainingOrPhoneContainingOrAccount_EmailContaining(String name, String phone, String email, Pageable pageable);
}
