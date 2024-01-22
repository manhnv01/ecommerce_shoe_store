package com.nvm.shoestoreapi.controller.site;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.dto.request.VerificationRequest;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.service.CustomerService;
import com.nvm.shoestoreapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.VERIFIED_SUCCESSFULLY;

@RestController
@CrossOrigin("http://localhost:4200")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmailService emailService;

    @PostMapping("register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegisterRequest registerRequest,
                                      BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        try {
            Customer customer = customerService.register(registerRequest);
            //emailService.sendVerificationCode(customer);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("verification-email-by-code")
    public ResponseEntity<?> verificationEmailByCode(@RequestBody VerificationRequest request) {
        try {
            customerService.verificationEmailByCode(request.getEmail(), request.getCode());
            return ResponseEntity.ok(Map.of("message", VERIFIED_SUCCESSFULLY));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("send_verification-email-by-code")
    public ResponseEntity<?> sendVerificationEmailByCode(@RequestParam("email") String email) {
        try {
            Customer customer = customerService.findByAccount_Email(email);
            emailService.sendVerificationCode(customer);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
