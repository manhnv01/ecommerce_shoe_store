package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.entity.Customer;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {
    void sendVerificationLink(Customer customer) throws MessagingException, UnsupportedEncodingException;
    void sendEmailForgotPassword(Customer customer) throws MessagingException, UnsupportedEncodingException;
    void sendVerificationCode(Customer customer) throws MessagingException, UnsupportedEncodingException;
}
