package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Cart;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface EmailService {
    void sendVerificationEmail(Account account, String siteURL) throws MessagingException, UnsupportedEncodingException;
    void SendEmailForgotPassword(Account account, String siteURL) throws MessagingException, UnsupportedEncodingException;
    //boolean verify(String verificationCode);
}
