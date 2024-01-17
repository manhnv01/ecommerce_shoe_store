package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.service.BrandService;
import com.nvm.shoestoreapi.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("admin/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping(value = "")
    public ResponseEntity<?> createBrand(
            @Valid @ModelAttribute BrandRequest brandRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        return ResponseEntity.ok(brandService.createBrand(brandRequest));
    }

    @PutMapping("")
    public ResponseEntity<?> update(
            @Valid @ModelAttribute BrandRequest brandRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(brandService.updateBrand(brandRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable("id") Long id){
        brandService.deleteBrandById(id);
        return ResponseEntity.ok("Xóa nhãn hàng thành công !");
    }

    @GetMapping("")
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.findAll();
        return ResponseEntity.ok(brands);
    }
}
