package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.*;
import com.nvm.shoestoreapi.dto.response.ProfileResponse;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface CustomerService {
    Customer register (RegisterRequest registerRequest);
    ProfileResponse findByEmail(String email);
    Customer updateProfile(ProfileRequest profileRequest);
    Page<Customer> findAll(Pageable pageable);
    Page<Customer> search(String search, Pageable pageable);
    long count();
    long countByStatus(boolean status);

    Page<Customer> findByStatus(boolean status, Pageable pageable);
    Optional<Customer> findById(Long id);
}
