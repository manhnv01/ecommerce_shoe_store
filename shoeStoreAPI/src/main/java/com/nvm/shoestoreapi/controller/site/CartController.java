package com.nvm.shoestoreapi.controller.site;

import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;


}
