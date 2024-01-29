package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;

public interface AccountService {
    void verificationEmailByCode(String email, String verificationCode);
    void reSendVerificationCode(Account account);
    void resetPassword (ResetPasswordRequest resetPasswordRequest);
    Account findByEmail(String email);
}
