package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Cart;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.entity.Wishlist;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.CustomerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public Customer register(RegisterRequest registerRequest) {
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException(DUPLICATE_EMAIL);
        }

        Account account = new Account();
        account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        account.setRoles(Collections.singletonList(roleRepository.findByName(ROLE_USER)));
        account.setEmail(registerRequest.getEmail());

        String randomCode = RandomStringUtils.randomNumeric(6);

        account.setVerificationCode(randomCode);
        account.setEnabled(false);
        account.setAccountNonLocked(true);
        account.setVerificationCodeExpirationDate(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        Account savedAccount = accountRepository.save(account);

        Customer customer = new Customer();
        customer.setId(new Date().getTime());
        customer.setName(registerRequest.getName());
        customer.setAccount(savedAccount);

        Customer savedCustomer = customerRepository.save(customer);

        Cart cart = new Cart();
        cart.setCustomer(savedCustomer);
        cartRepository.save(cart);
        savedCustomer.setCart(cart);

        return customerRepository.save(savedCustomer);
    }

    @Override
    public Customer findByEmail(String email) {
        if (customerRepository.findByAccount_Email(email) == null) {
            throw new RuntimeException(CUSTOMER_NOT_FOUND);
        }
        return customerRepository.findByAccount_Email(email);
    }
}
