package com.nvm.shoestoreapi.controller.site;

import com.nvm.shoestoreapi.dto.request.RegisterRequest;
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
                                      HttpServletRequest request,
                                      BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body("errorMessages");
        }
        try {
            Customer customer = customerService.register(registerRequest);
            String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
            emailService.sendVerificationEmail(customer.getAccount(), siteURL);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("code") String code) {
        try {
            if (customerService.verify(code))
                return ResponseEntity.ok(VERIFIED_SUCCESSFULLY);
            else return ResponseEntity.badRequest().body("Verification code is invalid");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
