package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("admin/receipt")
@CrossOrigin(origins = "http://localhost:4200")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody ReceiptRequest receiptRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(receiptService.createReceipt(receiptRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(receiptService.findByReceiptId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotals() {
        long total = receiptService.count();

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
            return ResponseEntity.ok().body(receiptService.findByEmployeeNameOrSupplierNameContaining(search, search, pageable));
        }

        return ResponseEntity.ok().body(receiptService.findAll(pageable));
    }
}
