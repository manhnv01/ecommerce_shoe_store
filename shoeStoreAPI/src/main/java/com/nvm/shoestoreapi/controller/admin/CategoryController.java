package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.service.CategoryService;
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
import java.util.List;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("admin/category")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("")
    public ResponseEntity<?> update(
            @Valid @RequestBody CategoryRequest categoryRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(categoryService.updateCategory(categoryRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{ids}")
    public ResponseEntity<?> updatesStatus(@PathVariable("ids") List<Long> ids,
                                           @RequestParam("enabled") boolean enabled) {
        try {
            categoryService.updatesStatus(ids, enabled);
            return ResponseEntity.ok().body(Collections.singletonMap("message", UPDATE_CATEGORY_STATUS_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(categoryService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_CATEGORY_SUCCESS));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getCategoryTotals() {
        long totalCategories = categoryService.count();
        long countByEnabledTrue = categoryService.countByEnabledTrue();
        long countByEnabledFalse = categoryService.countByEnabledFalse();

        Map<String, Long> totals = new HashMap<>();
        totals.put("totalCategories", totalCategories);
        totals.put("totalEnabledCategories", countByEnabledTrue);
        totals.put("totalDisabledCategories", countByEnabledFalse);

        return ResponseEntity.ok(totals);
    }

    @GetMapping({"/", ""})
    public ResponseEntity<?> getAll(
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "enabled", defaultValue = "", required = false) String enabled,
            @RequestParam(value = "size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {

        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (StringUtils.hasText(search)) {
            return ResponseEntity.ok().body(categoryService.findByNameContaining(search, pageable));
        }

        if (StringUtils.hasText(enabled)) {
            if (enabled.equalsIgnoreCase("true")) {
                return ResponseEntity.ok().body(categoryService.findByEnabled(true, pageable));
            } else if (enabled.equalsIgnoreCase("false")) {
                return ResponseEntity.ok().body(categoryService.findByEnabled(false, pageable));
            }
        }
        return ResponseEntity.ok().body(categoryService.findAll(pageable));
    }
}
