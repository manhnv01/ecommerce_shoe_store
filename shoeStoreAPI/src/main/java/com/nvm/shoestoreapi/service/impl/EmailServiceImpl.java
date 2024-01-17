package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Supplier;
import com.nvm.shoestoreapi.repository.SupplierRepository;
import com.nvm.shoestoreapi.service.EmailService;
import com.nvm.shoestoreapi.service.SupplierService;
import com.nvm.shoestoreapi.util.SlugUtil;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendVerificationEmail(Account account, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String verifyURL = REQUEST_URL + "/verify?code=" + account.getVerificationCode();
        String subject = "Xác minh tài khoản của bạn";
        String content = "Xin chào "+ account.getEmail() + ",<br>"
                + "Vui lòng nhấp vào liên kết bên dưới để xác minh đăng ký của bạn:<br>"
                + "<h3><a href=\"" + verifyURL + "\" target=\"_self\">Xác minh</a></h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn tạo tài khoản. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(account.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);

    }

    @Override
    public void SendEmailForgotPassword(Account account, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String verifyURL = siteURL + "/reset-password?code=" + account.getVerificationCode();
        String subject = "Xác minh thay đổi mật khẩu của bạn";
        String content = "Xin chào "+ account.getEmail() + ",<br>"
                + "Vui lòng nhấp vào liên kết bên dưới để xác minh đăng ký của bạn:<br>"
                + "<h3><a href=\"" + verifyURL + "\" target=\"_self\">Xác minh</a></h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn yêu cầu. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(account.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
