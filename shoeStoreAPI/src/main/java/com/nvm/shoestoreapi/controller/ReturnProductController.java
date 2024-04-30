package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.service.ReturnProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("api/return-product")
@CrossOrigin(origins = "http://localhost:4200")
public class ReturnProductController {

    @Autowired
    private ReturnProductService returnProductService;

    // TODO: API dành cho người quản trị
    @PostMapping({"/", ""})
    public ResponseEntity<?> create(@Valid @RequestBody ReturnProductRequest request, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            return ResponseEntity.ok().body(returnProductService.create(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping({"/", ""})
    public ResponseEntity<?> update(@Valid @RequestBody ReturnProductRequest request, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            return ResponseEntity.ok().body(returnProductService.update(request.getId(), request.getStatus(), request.getReason()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotals() {
        long total = returnProductService.count();
        long approved = returnProductService.countByStatus(RETURN_APPROVED);
        long pending = returnProductService.countByStatus(RETURN_PENDING);
        long rejected = returnProductService.countByStatus(RETURN_REJECTED);

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);
        totals.put("approved", approved);
        totals.put("pending", pending);
        totals.put("rejected", rejected);

        return ResponseEntity.ok(totals);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(returnProductService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping({"/", ""})
    public ResponseEntity<?> getAll(
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "status", defaultValue = "", required = false) String status,
            @RequestParam(value = "size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {

        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (StringUtils.hasText(search)) {
            return ResponseEntity.ok().body(returnProductService.findByEmployeeNameOrCustomerNameContaining(search, search, pageable));
        }

        if (StringUtils.hasText(status)) {
            return ResponseEntity.ok().body(returnProductService.findByStatus(status, pageable));
        }

        return ResponseEntity.ok().body(returnProductService.findAll(pageable));
    }

    // TODO: API dành cho người dùng

    @GetMapping("customer/totals")
    public ResponseEntity<?> getTotalsForCustomer() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        long total = returnProductService.countByCustomerAccountEmail(email);
        long approved = returnProductService.countByCustomerAccountEmailAndStatus(email, RETURN_APPROVED);
        long pending = returnProductService.countByCustomerAccountEmailAndStatus(email, RETURN_PENDING);
        long rejected = returnProductService.countByCustomerAccountEmailAndStatus(email, RETURN_REJECTED);

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);
        totals.put("approved", approved);
        totals.put("pending", pending);
        totals.put("rejected", rejected);

        return ResponseEntity.ok(totals);
    }

    // lấy tất cả đơn hàng của 1 customer theo email
    @GetMapping("/customer")
    public ResponseEntity<?> getOrdersByCustomerEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "page-size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {
        pageNumber = (pageNumber <= 0) ? 0 : (pageNumber - 1); // Nếu page <= 0 thì trả về page đầu tiên
        Sort sort = sortDir.equalsIgnoreCase(SORT_ORDER_DEFAULT) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return ResponseEntity.ok().body(returnProductService.findByCustomerAccountEmail(email, pageable));
    }
}