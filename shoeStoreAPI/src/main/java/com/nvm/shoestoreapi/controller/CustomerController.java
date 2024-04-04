package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.ProfileRequest;
import com.nvm.shoestoreapi.dto.request.RegisterRequest;
import com.nvm.shoestoreapi.entity.Customer;
import com.nvm.shoestoreapi.service.CustomerService;
import com.nvm.shoestoreapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;
import static com.nvm.shoestoreapi.util.Constant.SORT_BY_DEFAULT;

@RestController
@CrossOrigin("http://localhost:4200")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmailService emailService;

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
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
            emailService.sendVerificationCode(customer.getAccount().getEmail(), customer.getAccount().getVerificationCode());
            return ResponseEntity.ok().body(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("customer/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        try {
            return ResponseEntity.ok().body(customerService.findByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("customer")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileRequest profileRequest,
                                           BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        try {
            return ResponseEntity.ok().body(customerService.updateProfile(profileRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotals() {
        long total = customerService.count();
//        long countByEnabledTrue = customerService.countByStatus(WORKING);
//        long countByEnabledFalse = customerService.countByStatus(STOPPED_WORKING);

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);
//        totals.put("totalEnabled", countByEnabledTrue);
//        totals.put("totalDisabled", countByEnabledFalse);

        return ResponseEntity.ok(totals);
    }

    @GetMapping({"/", ""})
    public ResponseEntity<?> getAll(
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {

        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (StringUtils.hasText(search)) {
            return ResponseEntity.ok().body(customerService.search(search, pageable));
        }

//        if (StringUtils.hasText(status)) {
//            return ResponseEntity.ok().body(customerService.findByStatus(status, pageable));
//        }

        return ResponseEntity.ok().body(customerService.findAll(pageable));
    }
}
