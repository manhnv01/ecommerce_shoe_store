package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.CartDetailsRequest;
import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;

import static com.nvm.shoestoreapi.util.Constant.DELETE_CART_DETAILS_SUCCESS;
import static com.nvm.shoestoreapi.util.Constant.DELETE_CATEGORY_SUCCESS;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping({"/", ""})
    public ResponseEntity<?> addProductToCart(@Valid @RequestBody CartDetailsRequest cartDetailsRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(cartService.addProductToCart(cartDetailsRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getCartByUserEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok().body(cartService.getCartByUserEmail(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping({"/", ""})
    public ResponseEntity<?> updateProductQuantity(@RequestBody CartDetailsRequest cartDetailsRequest) {
        try {
            return ResponseEntity.ok().body(cartService.updateProductQuantity(cartDetailsRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            cartService.deleteCartDetailsById(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_CART_DETAILS_SUCCESS));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
