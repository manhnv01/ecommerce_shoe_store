package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Employee;
import com.nvm.shoestoreapi.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByIdNot(Long id, Pageable pageable);
    Employee findByAccount_Email(String email);
    Page<Employee> findByNameContainingOrPhoneContainingOrAccount_EmailContaining(String name, String phone, String email, Pageable pageable);
    Page<Employee> findByNameContainingOrPhoneContainingOrAccountEmailContainingAndIdNot(String name, String phone, String email, Long id, Pageable pageable);
}