package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.request.VnPaymentRequest;
import com.nvm.shoestoreapi.service.OrderService;
import com.nvm.shoestoreapi.service.impl.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.nvm.shoestoreapi.util.Constant.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private OrderService orderService;

    // TODO: API dành cho người dùng

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(orderService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody OrderRequest orderRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(orderService.create(orderRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody OrderRequest orderRequest, BindingResult result) {
        if (result.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok().body(orderService.update(orderRequest.getId(), orderRequest.getOrderStatus(), orderRequest.getCancelReason()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // TODO: Thanh toán đơn hàng với VNPAY
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@ModelAttribute VnPaymentRequest vnPaymentRequest,
                                           HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnPayUrl = vnPayService.createPayment(vnPaymentRequest.getAmount(), vnPaymentRequest.getOrderInfo(), baseUrl);
        return ResponseEntity.ok().body(Map.of("redirectUrl", vnPayUrl));
    }

    @GetMapping("/payment/vnpay")
    public void GetMapping(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int paymentStatus = vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");

        if (paymentStatus == 1) {
            orderService.updatePaymentStatus(Long.parseLong(orderInfo), true, sdf.parse(paymentTime));
            authentication.getAuthorities().forEach(grantedAuthority -> {
                if (grantedAuthority.getAuthority().equals(ROLE_USER)) {
                    try {
                        response.sendRedirect("http://localhost:4200/order-success/" + orderInfo);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        response.sendRedirect("http://localhost:4200/admin/order/" + orderInfo);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return;
        }

        authentication.getAuthorities().forEach(grantedAuthority -> {
            if (grantedAuthority.getAuthority().equals(ROLE_USER)) {
                try {
                    response.sendRedirect("http://localhost:4200/order-success/" + orderInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    response.sendRedirect("http://localhost:4200/admin/order/" + orderInfo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @GetMapping("customer/totals")
    public ResponseEntity<?> getTotals() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        long total = orderService.countByCustomerAccountEmail(email);
        //"0" Chờ xác nhận
        long countPending = orderService.countByCustomerAccountEmailAndOrderStatus(email, 0);
        //"1" Đã xác nhận
        long countConfirmed = orderService.countByCustomerAccountEmailAndOrderStatus(email, 1);
        //"2" Đang giao hàng
        long countShipping = orderService.countByCustomerAccountEmailAndOrderStatus(email, 2);
        //"3" Đã giao
        long countDelivered = orderService.countByCustomerAccountEmailAndOrderStatus(email, 3);
        //"4" Đã hủy
        long countCancelled = orderService.countByCustomerAccountEmailAndOrderStatus(email, 4);
        //"5" Đã trả hàng
        long countReturned = orderService.countByCustomerAccountEmailAndOrderStatus(email, 5);

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);
        totals.put("pending", countPending);
        totals.put("confirmed", countConfirmed);
        totals.put("shipping", countShipping);
        totals.put("delivered", countDelivered);
        totals.put("cancelled", countCancelled);
        totals.put("returned", countReturned);

        return ResponseEntity.ok(totals);
    }

    // lấy tất cả đơn hàng của 1 customer theo email
    @GetMapping("/customer")
    public ResponseEntity<?> getOrdersByCustomerEmail(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(value = "page-size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {
        pageNumber = (pageNumber <= 0) ? 0 : (pageNumber - 1); // Nếu page <= 0 thì trả về page đầu tiên
        Sort sort = sortDir.equalsIgnoreCase(SORT_ORDER_DEFAULT) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (StringUtils.hasText(filter)) {
            return ResponseEntity.ok().body(orderService.findByCustomerAccountEmailAndOrderStatus(email, Integer.parseInt(filter), pageable));
        }

        return ResponseEntity.ok().body(orderService.findByCustomerAccountEmail(email, pageable));
    }

    // TODO: API dành cho người quản trị
    // Lấy tất cả đơn hàng
    @GetMapping({"/", ""})
    public ResponseEntity<?> getAllOrders(
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @RequestParam(value = "filter", defaultValue = "", required = false) String filter,
            @RequestParam(value = "page-size", defaultValue = PAGE_SIZE_DEFAULT, required = false) Integer pageSize,
            @RequestParam(value = "page-number", defaultValue = PAGE_NUMBER_DEFAULT, required = false) Integer pageNumber,
            @RequestParam(value = "sort-direction", defaultValue = SORT_ORDER_DEFAULT, required = false) String sortDir,
            @RequestParam(value = "sort-by", defaultValue = SORT_BY_DEFAULT, required = false) String sortBy) {
        pageNumber = (pageNumber <= 0) ? 0 : (pageNumber - 1); // Nếu page <= 0 thì trả về page đầu tiên
        Sort sort = sortDir.equalsIgnoreCase(SORT_ORDER_DEFAULT) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

//        if (StringUtils.hasText(search)) {
//            return ResponseEntity.ok().body(orderService.searchOrder(search, pageable));
//        }

        if (StringUtils.hasText(filter)) {
            return ResponseEntity.ok().body(orderService.findByOrderStatus(Integer.parseInt(filter), pageable));
        }

        return ResponseEntity.ok().body(orderService.findAll(pageable));
    }

    @GetMapping("/totals")
    public ResponseEntity<?> getTotalsForAdmin() {

        long total = orderService.count();
        //"0" Chờ xác nhận
        long countPending = orderService.countByOrderStatus(0);
        //"1" Đã xác nhận
        long countConfirmed = orderService.countByOrderStatus(1);
        //"2" Đang giao hàng
        long countShipping = orderService.countByOrderStatus(2);
        //"3" Đã giao
        long countDelivered = orderService.countByOrderStatus(3);
        //"4" Đã hủy
        long countCancelled = orderService.countByOrderStatus(4);
        //"5" Đã trả hàng
        long countReturned = orderService.countByOrderStatus(5);

        Map<String, Long> totals = new HashMap<>();
        totals.put("total", total);
        totals.put("pending", countPending);
        totals.put("confirmed", countConfirmed);
        totals.put("shipping", countShipping);
        totals.put("delivered", countDelivered);
        totals.put("cancelled", countCancelled);
        totals.put("returned", countReturned);

        return ResponseEntity.ok(totals);
    }
}