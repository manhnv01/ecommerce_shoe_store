package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.repository.AccountRepository;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.RoleRepository;
import com.nvm.shoestoreapi.service.CustomerService;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

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
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public Customer register(RegisterRequest registerRequest) {
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException(DUPLICATE_EMAIL);
        }

        Account account = new Account();
        account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        account.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));
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
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByAccount_Email(String email) {
        return customerRepository.findByAccount_Email(email);
    }

    @Override
    public void verificationEmailByCode(String email, String verificationCode) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent() && account.get().getVerificationCode() != null && !account.get().isEnabled()) {
            if (account.get().getVerificationCode().equals(verificationCode)) {
                if (account.get().getVerificationCodeExpirationDate() == null
                        || account.get().getVerificationCodeExpirationDate().getTime() > System.currentTimeMillis()){
                    account.get().setVerificationCode(null);
                    account.get().setEnabled(true);
                    account.get().setVerificationCodeExpirationDate(null);
                    accountRepository.save(account.get());
                }
                else
                    throw new RuntimeException(THE_VERIFICATION_CODE_HAS_EXPIRED);
            }
            else
                throw new RuntimeException(INVALID_VERIFICATION_CODE);
        }
    }
}
