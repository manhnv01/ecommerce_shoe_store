package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductRequest productRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(
            @Valid @RequestBody ProductRequest productRequest,
            BindingResult result,
            @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Xóa danh mục thành công !");
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }
}
