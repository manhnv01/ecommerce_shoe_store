package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("api/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    @Autowired
    private ProductService productService;


    // TODO: API dành cho người quản trị
    @PostMapping({"/", ""})
    public ResponseEntity<?> create(@Valid @ModelAttribute ProductRequest productRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(productService.create(productRequest));
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
            return ResponseEntity.ok().body(productService.update(productRequest));
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
            return ResponseEntity.ok().body(productService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/product-details/{id}")
    public ResponseEntity<?> getProductDetailsId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(productService.findByProductDetailsId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll() {
        try {
            return ResponseEntity.ok().body(productService.findAll());
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
        String path = request.getRequestURI().substring(request.getContextPath().length() + "/api/product/".length() + id.toString().length() + "/images/".length());
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
    public ResponseEntity<Page<ProductResponse>> getAll(
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "enabled", defaultValue = "") String enabled,
            @RequestParam(value = "isZeroQuantity", defaultValue = "") String isZeroQuantity,
            @RequestParam(value = "size", defaultValue = PAGE_SIZE_DEFAULT) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT) String sortBy) {

        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (StringUtils.hasText(search)) {
            return ResponseEntity.ok().body(productService.search(search, pageable));
        }

        if (StringUtils.hasText(enabled)) {
            if (enabled.equalsIgnoreCase("true")) {
                return ResponseEntity.ok().body(productService.searchByStatus(true, pageable));
            } else if (enabled.equalsIgnoreCase("false")) {
                return ResponseEntity.ok().body(productService.searchByStatus(false, pageable));
            }
        }

        if (StringUtils.hasText(isZeroQuantity)) {
            if (isZeroQuantity.equalsIgnoreCase("true")) {
                return ResponseEntity.ok().body(productService.getProductsByTotalQuantity(pageable, true));
            } else if (isZeroQuantity.equalsIgnoreCase("false")) {
                return ResponseEntity.ok().body(productService.getProductsByTotalQuantity(pageable, false));
            }
        }
        return ResponseEntity.ok().body(productService.getAll(pageable));
    }

    // TODO: API dành cho người dùng

    // Lấy ra sản phẩm theo slug
    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> getBySlug(@PathVariable String slug) {
        try {
            return ResponseEntity.ok().body(productService.findBySlug(slug));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy ra 10 sản phẩm mới nhất
    @GetMapping("/newest")
    public ResponseEntity<?> findTop10ByEnabledIsTrueOrderByCreatedAtDesc() {
        try {
            return ResponseEntity.ok().body(productService.findTop10ByEnabledIsTrueOrderByCreatedAtDesc());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy ra 10 sản phẩm liên quan có cùng category và brand
    @GetMapping("/similar-product")
    public ResponseEntity<?> findTop10ByCategory_IdAndBrand_IdAndEnabledIsTrue(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "brandId", required = false) Long brandId) {
        try {
            return ResponseEntity.ok().body(productService.findTop10ByCategory_IdAndBrand_IdAndEnabledIsTrue(categoryId, brandId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy ra tất cả sản phẩm có trạng thái enabled = true
    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(value = "size", defaultValue = USER_PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {
        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return ResponseEntity.ok().body(productService.findAllByEnabledIsTrue(pageable));
    }

    // Lấy ra tất cả sản phẩm có trạng thái enabled = true và brand_slug = slug
    @GetMapping("/brand/{slug}")
    public ResponseEntity<?> findByEnabledIsTrueAndBrand_Slug(
            @PathVariable String slug,
            @RequestParam(value = "size", defaultValue = USER_PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {
        pageNumber = Math.max(pageNumber, 1) - 1;
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return ResponseEntity.ok().body(productService.findByEnabledIsTrueAndBrand_Slug(slug, pageable));
    }

    // Lọc sản phẩm
    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(value = "brands", required = false) List<String> brands,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "price-min", required = false) Long priceMin,
            @RequestParam(value = "price-max", required = false) Long priceMax,
            @RequestParam(value = "size", defaultValue = USER_PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {
        pageNumber = (pageNumber <= 0) ? 0 : (pageNumber - 1); // Nếu page <= 0 thì trả về page đầu tiên
        Sort sort = sortDir.equalsIgnoreCase(SORT_ORDER_DEFAULT) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return ResponseEntity.ok().body(productService.filter(brands, categories, priceMin, priceMax, pageable));
    }
}
