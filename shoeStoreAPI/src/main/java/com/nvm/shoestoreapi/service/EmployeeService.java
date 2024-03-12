package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.EmployeeRequest;
import com.nvm.shoestoreapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Page<Employee> findAll(Pageable pageable);

    Page<Employee> searchEmployee(String search, Pageable pageable);

    Page<Employee> findByEnabled(boolean enabled, Pageable pageable);

    Employee create(EmployeeRequest employeeRequest);

    Employee update(EmployeeRequest employeeRequest);

    long count();
    Optional<Employee> findById(Long id);

    void deleteById(Long id);
}
