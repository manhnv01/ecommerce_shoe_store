package com.nvm.shoestoreapi.controller.site;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.dto.request.VerificationRequest;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.service.AccountService;
import com.nvm.shoestoreapi.service.CustomerService;
import com.nvm.shoestoreapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.RESET_PASSWORD_SUCCESSFULLY;
import static com.nvm.shoestoreapi.util.Constant.VERIFIED_SUCCESSFULLY;

@RestController
@CrossOrigin("http://localhost:4200")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;

    @PostMapping("verification-email-by-code")
    public ResponseEntity<?> verificationEmailByCode(@RequestBody VerificationRequest request) {
        try {
            accountService.verificationEmailByCode(request.getEmail(), request.getCode());
            return ResponseEntity.ok(Map.of("message", VERIFIED_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("send-verification-email-by-code")
    public ResponseEntity<?> sendVerificationEmailByCode(@RequestParam("email") String email) {
        try {
            Account account = accountService.findByEmail(email);
            accountService.reSendVerificationCode(account);
            emailService.sendVerificationCode(account.getEmail(), account.getVerificationCode());
            return ResponseEntity.ok(Map.of("message", VERIFIED_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        try {
            Account account = accountService.findByEmail(email);
            accountService.reSendVerificationCode(account);
            emailService.sendEmailForgotPassword(account.getEmail(), account.getVerificationCode());
            return ResponseEntity.ok(Map.of("message", VERIFIED_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest,
                                           BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        try {
            accountService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok(Map.of("message", RESET_PASSWORD_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("send-verification-forgot-password-by-code")
    public ResponseEntity<?> sendVerificationForgotPasswordByCode(@RequestParam("email") String email) {
        try {
            //Customer customer = customerService.findByAccount_Email(email);
            //customerService.reSendVerificationCode(customer);
            //emailService.sendEmailForgotPassword(customer.getAccount().getEmail(), customer.getName(), customer.getAccount().getVerificationCode());
            return ResponseEntity.ok(Map.of("message", VERIFIED_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
