package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/report")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

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

    @GetMapping("/count-by-category")
    private ResponseEntity<?> getProductCountByCategory() {
        return ResponseEntity.ok().body(orderService.getProductCountByCategory());
    }
    @GetMapping("/count-by-brand")
    private ResponseEntity<?> getProductCountByBrand() {
        return ResponseEntity.ok().body(orderService.getProductCountByBrand());
    }

    // Lấy top 5 sản phẩm bán chạy nhất
    @GetMapping("/top-5-best-seller")
    private ResponseEntity<?> getTop5BestSeller(
            @RequestParam("month") int month,
            @RequestParam("year") int year
    ) {
        Pageable pageable = PageRequest.of(0, 5);
        return ResponseEntity.ok().body(productService.findProductsByOrderStatusAndDate(month, year, pageable));
    }

    // lấy doanh thu theo năm
    @GetMapping("/revenue-by-year")
    private ResponseEntity<?> getRevenueByYear(@RequestParam("year") int year) {
        return ResponseEntity.ok().body(productService.findRevenueByYear(year));
    }

    // lâ chi phí theo năm
    @GetMapping("/cost-by-year")
    private ResponseEntity<?> getCostByYear(@RequestParam("year") int year) {
        return ResponseEntity.ok().body(productService.findCostByYear(year));
    }
}
