package com.nvm.shoestoreapi.controller;

import com.nvm.shoestoreapi.dto.request.VnPaymentRequest;
import com.nvm.shoestoreapi.service.BrandService;
import com.nvm.shoestoreapi.service.impl.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@RequestMapping("api/brand")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private VNPayService vnPayService;
    //private final OrderService orderService;

    // TODO: API dành cho người quản trị
    // TODO: API dành cho người dùng

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@ModelAttribute VnPaymentRequest vnPaymentRequest,
                                           HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnPayUrl = vnPayService.createPayment(vnPaymentRequest.getAmount(), vnPaymentRequest.getOrderInfo(), baseUrl);
        return ResponseEntity.ok().body(Map.of("redirectUrl", vnPayUrl));
    }

    @GetMapping("/vnpay-payment")
    public void GetMapping(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int paymentStatus = vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");

        if (paymentStatus == 1) {
 //           orderService.updatePaymentStatus(Long.parseLong(orderInfo), true, sdf.parse(paymentTime));
            authentication.getAuthorities().forEach(grantedAuthority -> {
                if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                    try {
                        response.sendRedirect("http://localhost:4200/thanh-toan-thanh-cong/" + orderInfo);
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
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                try {
                    response.sendRedirect("http://localhost:4200/don-hang/" + orderInfo);
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
}