package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.repository.AccountRepository;
import com.nvm.shoestoreapi.repository.CustomerRepository;
import com.nvm.shoestoreapi.repository.RoleRepository;
import com.nvm.shoestoreapi.service.AccountService;
import com.nvm.shoestoreapi.service.CustomerService;
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
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void verificationEmailByCode(String email, String verificationCode) {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (!account.isEnabled()) {
                checkVerificationCode(account, verificationCode);
            } else {
                throw new RuntimeException(ACCOUNT_ALREADY_VERIFIED);
            }
        } else {
            throw new RuntimeException(DOES_NOT_EXIST);
        }
    }

    private void checkVerificationCode(Account account, String verificationCode) {
        String storedVerificationCode = account.getVerificationCode();
        Date expirationDate = account.getVerificationCodeExpirationDate();

        if (storedVerificationCode != null && storedVerificationCode.equals(verificationCode)) {
            if (expirationDate == null || expirationDate.getTime() > System.currentTimeMillis()) {
                account.setVerificationCode(null);
                account.setEnabled(true);
                account.setVerificationCodeExpirationDate(null);
                accountRepository.save(account);
            } else {
                throw new RuntimeException(THE_VERIFICATION_CODE_HAS_EXPIRED);
            }
        } else {
            throw new RuntimeException(INVALID_VERIFICATION_CODE);
        }
    }

    @Override
    public void reSendVerificationCode(Account account) {
        String verificationCode = RandomStringUtils.randomNumeric(6);
        account.setVerificationCode(verificationCode);
        account.setVerificationCodeExpirationDate(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        accountRepository.save(account);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Optional<Account> accountOptional = accountRepository.findByEmail(resetPasswordRequest.getEmail());

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            String storedVerificationCode = account.getVerificationCode();
            Date expirationDate = account.getVerificationCodeExpirationDate();

            if (storedVerificationCode != null && storedVerificationCode.equals(resetPasswordRequest.getCode())) {
                if (expirationDate == null || expirationDate.getTime() > System.currentTimeMillis()) {
                    account.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequest.getNewPassword()));
                    account.setVerificationCode(null);
                    account.setVerificationCodeExpirationDate(null);
                    accountRepository.save(account);
                } else {
                    throw new RuntimeException(THE_VERIFICATION_CODE_HAS_EXPIRED);
                }
            } else {
                throw new RuntimeException(INVALID_VERIFICATION_CODE);
            }
        } else {
            throw new RuntimeException(DOES_NOT_EXIST);
        }
    }

    @Override
    public Account findByEmail(String email) {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        }
        throw new RuntimeException(DOES_NOT_EXIST);
    }
}
