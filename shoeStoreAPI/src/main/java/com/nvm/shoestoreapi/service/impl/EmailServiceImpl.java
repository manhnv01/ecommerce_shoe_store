package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationLink(Customer customer)
            throws MessagingException, UnsupportedEncodingException {
        String verifyURL = REQUEST_URL + "/verify?code=" + customer.getAccount().getVerificationCode();
        String subject = "Xác minh địa chỉ email của bạn";
        String content = "Xin chào " + customer.getAccount().getEmail() + ",<br>"
                + "Vui lòng nhấp vào liên kết bên dưới để xác minh đăng ký của bạn:<br>"
                + "<h3><a href=\"" + verifyURL + "\" target=\"_self\">Xác minh</a></h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn tạo tài khoản. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Email này được được gửi tự động. Vui lòng không trả lời email này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(customer.getAccount().getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);

    }

    @Override
    public void sendEmailForgotPassword(Customer customer) throws MessagingException, UnsupportedEncodingException {
        String verifyURL = REQUEST_URL + "/reset-password?code=" + customer.getAccount().getVerificationCode();
        String subject = "Xác minh thay đổi mật khẩu của bạn";
        String content = "Xin chào " + customer.getName() + ",<br>"
                + "Bạn đã yêu cầu xác minh email "+ customer.getAccount().getEmail() + ":<br>"
                + "Vui lòng nhấp vào liên kết bên dưới để xác minh đăng ký của bạn:<br>"
                + "<h3><a href=\"" + verifyURL + "\" target=\"_self\">Xác minh</a></h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn yêu cầu. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Email này được được gửi tự động. Vui lòng không trả lời email này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(customer.getAccount().getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void sendVerificationCode(Customer customer) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String verificationCode = customer.getAccount().getVerificationCode();
        String subject = "Xác minh địa chỉ email của bạn";
        String content = "Xin chào " + customer.getName() + ",<br>"
                + "Bạn đã yêu cầu xác minh email "+ customer.getAccount().getEmail() + ":<br>"
                + "Để tiếp tục, vui lòng nhập mã xác minh:<br>"
                + "<h3>" + verificationCode + "</h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn yêu cầu. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Email này được được gửi tự động. Vui lòng không trả lời email này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(customer.getAccount().getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
