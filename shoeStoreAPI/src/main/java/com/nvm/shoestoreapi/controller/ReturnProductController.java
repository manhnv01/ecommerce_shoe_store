package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.service.ReturnProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

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

    // TODO: API dành cho người dùng
}