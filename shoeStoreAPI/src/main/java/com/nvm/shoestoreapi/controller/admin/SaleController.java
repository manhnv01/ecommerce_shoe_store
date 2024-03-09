package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.SaleRequest;
import com.nvm.shoestoreapi.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("api/sale")
@CrossOrigin(origins = "http://localhost:4200")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody SaleRequest saleRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(saleService.create(saleRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("")
    public ResponseEntity<?> update(
            @Valid @RequestBody SaleRequest supplierRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(saleService.update(supplierRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(saleService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate-product-in-sale")
    public ResponseEntity<?> validateProductInSale(@RequestBody SaleRequest saleRequest) {
        try {
            saleService.validateProductInSale(saleRequest);
            return ResponseEntity.ok().body(Collections.singletonMap("message", PRODUCT_VALID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            saleService.deleteById(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_SALE_SUCCESS));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotals() {
        long total = saleService.count();

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);

        return ResponseEntity.ok(totals);
    }

    @GetMapping({"/", ""})
    public ResponseEntity<?> getAll(
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {

        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (StringUtils.hasText(search)) {
            return ResponseEntity.ok().body(saleService.findByNameContaining(search, pageable));
        }

        return ResponseEntity.ok().body(saleService.findAll(pageable));
    }
}
