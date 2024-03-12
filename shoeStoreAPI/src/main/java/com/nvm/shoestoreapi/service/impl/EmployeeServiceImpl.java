package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ProductMapper;
import com.nvm.shoestoreapi.dto.request.EmployeeRequest;
import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.EmployeeService;
import com.nvm.shoestoreapi.service.ProductService;
import com.nvm.shoestoreapi.service.StorageService;
import com.nvm.shoestoreapi.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> searchEmployee(String search, Pageable pageable) {
        return employeeRepository.findByNameContainingOrPhoneContainingOrAccount_EmailContaining(search, search, search, pageable);
    }

    @Override
    public Page<Employee> findByEnabled(boolean enabled, Pageable pageable) {
        return null;
    }

    @Override
    public Employee create(EmployeeRequest employeeRequest) {
        return null;
    }

    @Override
    public Employee update(EmployeeRequest employeeRequest) {
        return null;
    }

    @Override
    public long count() {
        return employeeRepository.count();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }
}
