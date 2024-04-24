package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
