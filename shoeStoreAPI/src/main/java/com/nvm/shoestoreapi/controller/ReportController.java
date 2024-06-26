package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.service.OrderService;
import com.nvm.shoestoreapi.service.ProductService;
import com.nvm.shoestoreapi.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("api/report")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {


    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReportService reportService;

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

    // lấy danh sản phẩm đang được quan tam nhiều nhất
    @GetMapping("/product-interest")
    private ResponseEntity<?> getProductInterest(
            @RequestParam(value = "size", defaultValue = PAGE_SIZE_DEFAULT) Integer pageSize,
            @RequestParam(value = "page", defaultValue = PAGE_NUMBER_DEFAULT) Integer pageNumber) {

        pageNumber = Math.max(pageNumber, 1) - 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(productService.findProductInterest(pageable));
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

    @GetMapping("/cost-return-by-year")
    private ResponseEntity<?> getCostReturnByYear(@RequestParam("year") int year) {
        return ResponseEntity.ok().body(productService.findCostReturnByYear(year));
    }

    /////////////////////////////////////////////////////

    @GetMapping("/order")
    public ResponseEntity<?> getAllSaleReport(@RequestParam(value = "year", required = false) Integer year) {
        return ResponseEntity.ok().body(reportService.getOrderReport(year));
    }

    @GetMapping("/receipt")
    public ResponseEntity<?> getAllReceiptReport(@RequestParam(value = "year", required = false) Integer year) {
        return ResponseEntity.ok().body(reportService.getReceiptReport(year));
    }

    @GetMapping("/inventory/export")
    public ResponseEntity<?> exportInventoryReport() {
        return ResponseEntity.ok().body(reportService.exportProductReport());
    }

    @GetMapping("/order/export")
    public ResponseEntity<?> exportOrderReport(@RequestParam(value = "year", required = false) Integer year) {
        return ResponseEntity.ok().body(reportService.exportOrderReport(year));
    }

    @GetMapping("/receipt/export")
    public ResponseEntity<?> exportReceiptReport(@RequestParam(value = "year", required = false) Integer year) {
        return ResponseEntity.ok().body(reportService.exportReceiptReport(year));
    }
}
