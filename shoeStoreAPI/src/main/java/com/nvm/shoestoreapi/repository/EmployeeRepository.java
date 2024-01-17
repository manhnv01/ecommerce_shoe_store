package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}