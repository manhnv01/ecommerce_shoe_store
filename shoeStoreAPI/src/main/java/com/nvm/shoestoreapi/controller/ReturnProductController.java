package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.service.ReturnProductService;
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

import static com.nvm.shoestoreapi.util.Constant.*;
import static com.nvm.shoestoreapi.util.Constant.SORT_BY_DEFAULT;

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

//        if (StringUtils.hasText(search)) {
//            return ResponseEntity.ok().body(returnProductService.findByEmployeeNameOrSupplierNameContaining(search, search, pageable));
//        }

        return ResponseEntity.ok().body(returnProductService.findAll(pageable));
    }

    // TODO: API dành cho người dùng
}