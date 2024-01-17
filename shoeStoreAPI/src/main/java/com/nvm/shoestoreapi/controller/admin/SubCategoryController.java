package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.SubCategoryRequest;
import com.nvm.shoestoreapi.entity.SubCategory;
import com.nvm.shoestoreapi.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/sub-category")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping("")
    public ResponseEntity<?> createSubCategory(@Valid @RequestBody SubCategoryRequest subCategoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(subCategoryService.createSubCategory(subCategoryRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateSubCategory(
            @Valid @RequestBody SubCategoryRequest subCategoryRequest,
            BindingResult result,
            @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(subCategoryService.updateSubCategory(id, subCategoryRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable("id") Long id){
        subCategoryService.deleteSubCategoryById(id);
        return ResponseEntity.ok("Xóa danh mục thành công !");
    }

    @GetMapping("")
    public ResponseEntity<List<SubCategory>> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryService.findAll();
        return ResponseEntity.ok(subCategories);
    }
}
