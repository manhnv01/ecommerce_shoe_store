package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.service.BrandService;
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
@RequestMapping("api/brand")
@CrossOrigin(origins = "http://localhost:4200")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // TODO: API dành cho người quản trị
    @PostMapping({"/", ""})
    public ResponseEntity<?> create(@Valid @ModelAttribute BrandRequest brandRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            return ResponseEntity.ok().body(brandService.create(brandRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping({"/", ""})
    public ResponseEntity<?> update(@Valid @ModelAttribute BrandRequest brandRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(brandService.update(brandRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{ids}")
    public ResponseEntity<?> updatesStatus(@PathVariable("ids") List<Long> ids, @RequestParam("enabled") boolean enabled) {
        try {
            brandService.updatesStatus(ids, enabled);
            return ResponseEntity.ok().body(Collections.singletonMap("message", UPDATE_BRAND_STATUS_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(brandService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            brandService.deleteById(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_BRAND_SUCCESS));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotals() {
        long totalCategories = brandService.count();
        long countByEnabledTrue = brandService.countByEnabledTrue();
        long countByEnabledFalse = brandService.countByEnabledFalse();

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", totalCategories);
        totals.put("totalEnabled", countByEnabledTrue);
        totals.put("totalDisabled", countByEnabledFalse);

        return ResponseEntity.ok(totals);
    }

    @PutMapping("/switch/{id}")
    public ResponseEntity<?> changeSwitch(@PathVariable Long id) {
        try {
            brandService.changeSwitch(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", CHANGE_SWITCH_BRAND_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
            return ResponseEntity.ok().body(brandService.findByNameContaining(search, pageable));
        }

        if (StringUtils.hasText(enabled)) {
            if (enabled.equalsIgnoreCase("true")) {
                return ResponseEntity.ok().body(brandService.findByEnabled(true, pageable));
            } else if (enabled.equalsIgnoreCase("false")) {
                return ResponseEntity.ok().body(brandService.findByEnabled(false, pageable));
            }
        }
        return ResponseEntity.ok().body(brandService.findAll(pageable));
    }

    // TODO: API dành cho người dùng

    // Lấy tất cả các thương hiệu có enabled = true
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(brandService.findByEnabledIsTrue());
    }
}