package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ChangePasswordRequest;
import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;

public interface AccountService {
    void verificationEmailByCode(String email, String verificationCode);
    void generateVerificationCode(Account account);
    void resetPassword (ResetPasswordRequest resetPasswordRequest);
    void changePassword (ChangePasswordRequest changePasswordRequest);
    Account findByEmail(String email);
    boolean existsByEmail(String email);
}
