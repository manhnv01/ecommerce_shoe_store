package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ExportService exportService;

    @Override
    public void sendVerificationLink(String email, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        String verifyURL = REQUEST_URL + "/verify?code=" + verificationCode;
        String subject = "Xác minh địa chỉ email của bạn";
        String content = "Vui lòng nhấp vào liên kết bên dưới để xác minh đăng ký của bạn:<br>"
                + "<h3><a href=\"" + verifyURL + "\" target=\"_self\">Xác minh</a></h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn tạo tài khoản. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Email này được được gửi tự động. Vui lòng không trả lời email này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;
        sendEmail(email, subject, content);
    }

    @Override
    public void sendEmailForgotPassword(String email, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        String subject = "Đặt lại mật khẩu của bạn";
        String content = "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản liên kết với email "+ email + ":<br>"
                + "Để tiếp tục, vui lòng nhập mã xác minh sau đây để xác nhận yêu cầu đặt lại mật khẩu:<br>"
                + "<h3>" + verificationCode + "</h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn yêu cầu, xin hãy bỏ qua tin nhắn này.<br>"
                + "Email này được gửi tự động. Vui lòng không trả lời email này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        sendEmail(email, subject, content);
    }

    @Override
    public void sendVerificationCode(String email, String verificationCode) throws MessagingException, UnsupportedEncodingException {
        String subject = "Xác minh địa chỉ email của bạn";
        String content = "Bạn đã yêu cầu xác minh email "+ email + ":<br>"
                + "Để tiếp tục, vui lòng nhập mã xác minh sau đây để xác nhận:<br>"
                + "<h3>" + verificationCode + "</h3>"
                + "Đường dẫn này sẽ hết hạn trong 5 phút.<br>"
                + "Nếu không phải bạn yêu cầu. Xin hãy bỏ qua tin nhắn này.<br>"
                + "Email này được được gửi tự động. Vui lòng không trả lời email này.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        sendEmail(email, subject, content);
    }

    @Override
    public void sendBill(String email, Long orderId) throws MessagingException, UnsupportedEncodingException {
        String subject = "Xác nhận đơn đặt hàng của bạn";
        String content = "Kính gửi Quý khách hàng,<br>"
                + "Cảm ơn bạn đã ủng hộ cửa hàng Shoes Station<br>"
                + "Ðể xem chi tiết đơn hàng, Quý khách hàng vui lòng tải PDF xuống.<br>"
                + "Trân trọng,<br>"
                + STORE_NAME;

        try {
            sendEmailWithEmbeddedPDF(email, subject, content, exportService.generateInvoicePdf(orderId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String email, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    private void sendEmailWithEmbeddedPDF(String email, String subject, String content, byte[] pdfData) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(FROM_EMAIL, STORE_NAME);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("MM_dd_yyyy"));

        helper.addAttachment("invoice_shoes_station_" + formattedDateTime + ".pdf", new ByteArrayResource(pdfData));

        mailSender.send(message);
    }

}
