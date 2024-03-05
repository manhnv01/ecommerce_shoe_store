package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static com.nvm.shoestoreapi.util.Constant.*;
import static com.nvm.shoestoreapi.util.Constant.SORT_BY_DEFAULT;

@RestController
@RequestMapping("admin/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping({"/", ""})
    public ResponseEntity<?> create(@Valid @ModelAttribute ProductRequest productRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(productService.createProduct(productRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping({"/", ""})
    public ResponseEntity<?> update(@Valid @ModelAttribute ProductRequest productRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(productService.updateProduct(productRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{ids}")
    public ResponseEntity<?> updatesStatus(@PathVariable("ids") List<Long> ids, @RequestParam("enabled") boolean enabled) {
        try {
            productService.updatesStatus(ids, enabled);
            return ResponseEntity.ok().body(Collections.singletonMap("message", UPDATE_PRODUCT_STATUS_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(productService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotals() {
        long total = productService.count();
        long countByEnabledTrue = productService.countByEnabledTrue();
        long countByEnabledFalse = productService.countByEnabledFalse();

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);
        totals.put("totalEnabled", countByEnabledTrue);
        totals.put("totalDisabled", countByEnabledFalse);

        return ResponseEntity.ok(totals);
    }

    @DeleteMapping("/{id}/images/**")
    public ResponseEntity<?> deleteImage(HttpServletRequest request, @PathVariable Long id) {
        String path = request.getRequestURI().substring(request.getContextPath().length() + "/admin/product/".length() + id.toString().length() + "/images/".length());
        try {
            productService.deleteImageById(id, path);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_PRODUCT_IMAGE_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/colors/{id}")
    public ResponseEntity<?> deleteProductColors(@PathVariable Long id) {
        try {
            productService.deleteProductColor(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_PRODUCT_COLOR_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", DELETE_PRODUCT_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/switch/{id}")
    public ResponseEntity<?> changeSwitch(@PathVariable Long id) {
        try {
            productService.changeSwitch(id);
            return ResponseEntity.ok().body(Collections.singletonMap("message", CHANGE_SWITCH_PRODUCT_SUCCESS));
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
            return ResponseEntity.ok().body(productService.findByNameContaining(search, pageable));
        }

        if (StringUtils.hasText(enabled)) {
            if (enabled.equalsIgnoreCase("true")) {
                return ResponseEntity.ok().body(productService.findByEnabled(true, pageable));
            } else if (enabled.equalsIgnoreCase("false")) {
                return ResponseEntity.ok().body(productService.findByEnabled(false, pageable));
            }
        }
        return ResponseEntity.ok().body(productService.findAll(pageable));
    }
}
