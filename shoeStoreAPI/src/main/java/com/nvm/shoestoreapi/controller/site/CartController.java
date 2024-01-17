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

    @PostMapping(value = "")
    public ResponseEntity<?> addToCart(
            @Valid @ModelAttribute CartRequest cartRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        return ResponseEntity.ok(cartService.addProductToCart(cartRequest));
    }

//    @PutMapping("{id}")
//    public ResponseEntity<?> updateBrand(
//            @Valid @RequestBody BrandRequest brandRequest,
//            BindingResult result,
//            @PathVariable("id") Long id) {
//        if (result.hasErrors()) {
//            return ResponseEntity.badRequest().body(result.getFieldError());
//        }
//        return ResponseEntity.ok(brandService.updateBrand(id, brandRequest));
//    }
//
//    @DeleteMapping("{id}")
//    public ResponseEntity<String> deleteBrand(@PathVariable("id") Long id){
//        brandService.deleteBrandById(id);
//        return ResponseEntity.ok("Xóa nhãn hàng thành công !");
//    }
//
//    @GetMapping("")
//    public ResponseEntity<List<Brand>> getAllBrands() {
//        List<Brand> brands = brandService.findAll();
//        return ResponseEntity.ok(brands);
//    }
}
