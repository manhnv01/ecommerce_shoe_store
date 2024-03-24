package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.LoginRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.dto.request.VerificationRequest;
import com.nvm.shoestoreapi.dto.response.LoginResponse;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.security.MyUserDetails;
import com.nvm.shoestoreapi.security.jwt.JwtTokenProvider;
import com.nvm.shoestoreapi.service.AccountService;
import com.nvm.shoestoreapi.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService emailService;

    final JwtTokenProvider tokenProvider;
    final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (!accountService.existsByEmail(loginRequest.getEmail()))
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", ACCOUNT_NOT_FOUND));

            // Xác thực từ username và password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Nếu thông tin hợp lệ -> Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Trả về jwt cho người dùng.
            String jwt = tokenProvider.generateToken((MyUserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (LockedException ex) {
            // Người dùng bị khóa
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", ACCOUNT_IS_LOCKED));
        } catch (BadCredentialsException e) {
            // Người dùng nhập sai mật khẩu
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap("message", INVALID_PASSWORD));
        }
    }

    @PostMapping("verification-email-by-code")
    public ResponseEntity<?> verificationEmailByCode(@RequestBody VerificationRequest request) {
        try {
            accountService.verificationEmailByCode(request.getEmail(), request.getCode());
            return ResponseEntity.ok().body(Collections.singletonMap("message", VERIFIED_SUCCESSFULLY));
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
            return ResponseEntity.ok().body(Collections.singletonMap("message", VERIFIED_SUCCESSFULLY));
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
            return ResponseEntity.ok().body(Collections.singletonMap("message", VERIFIED_SUCCESSFULLY));
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
            return ResponseEntity.ok().body(Collections.singletonMap("message", RESET_PASSWORD_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("send-verification-forgot-password-by-code")
    public ResponseEntity<?> sendVerificationForgotPasswordByCode(@RequestParam("email") String email) {
        try {
            Account account = accountService.findByEmail(email);
            accountService.reSendVerificationCode(account);
            emailService.sendEmailForgotPassword(account.getEmail(), account.getVerificationCode());
            return ResponseEntity.ok().body(Collections.singletonMap("message", VERIFIED_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
