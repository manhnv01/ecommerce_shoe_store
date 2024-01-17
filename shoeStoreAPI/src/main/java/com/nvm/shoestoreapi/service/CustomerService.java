package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface CustomerService {
    Customer register (RegisterRequest registerRequest);
    boolean verify(String verificationCode);
}
