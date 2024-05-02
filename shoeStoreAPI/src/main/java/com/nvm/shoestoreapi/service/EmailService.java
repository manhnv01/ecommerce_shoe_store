package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.entity.Customer;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendVerificationLink(String email, String verificationCode) throws MessagingException, UnsupportedEncodingException;

    void sendEmailForgotPassword(String email, String verificationCode) throws MessagingException, UnsupportedEncodingException;

    void sendVerificationCode(String email, String verificationCode) throws MessagingException, UnsupportedEncodingException;

    void sendBill(String email, Long orderId) throws MessagingException, UnsupportedEncodingException;
}
