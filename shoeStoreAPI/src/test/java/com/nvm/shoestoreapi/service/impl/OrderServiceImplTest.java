package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.OrderDetailsRequest;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.ProductColor;
import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.repository.ProductColorRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {
    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    ProductDetailsRepository productDetailsRepository;


    @Autowired
    private AuthenticationManager authenticationManager;
    private void setUserDefaultLogin(){
        List<String> roles = Collections.singletonList("ROLE_USER");

        // Tạo một UserDetails mặc định với username và password mặc định
        UserDetails defaultUser = User.withUsername("manonguyen123@gmail.com")
                .password("123456")
                .roles(String.valueOf(roles)) // Gán role cho user mặc định nếu cần
                .build();

        // Xác thực từ username và password mặc định.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(defaultUser.getUsername(), defaultUser.getPassword())
        );

        // Nếu thông tin hợp lệ -> Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

//    @Test
//    public void testCreateReceiptSuccess() {
//        // Chuẩn bị (Arrange)
//        ReceiptRequest receiptRequest = new ReceiptRequest();
//        receiptRequest.setEmployeeId(1111111111111L);
//        receiptRequest.setSupplierId(1L);
//        List<ReceiptDetailsRequest> receiptDetailsRequests = Arrays.asList(
//                new ReceiptDetailsRequest(1l,23,100l,34l)
//        );
//        receiptRequest.setReceiptDetails(receiptDetailsRequests);
//        ReceiptResponse createdReceipt = receiptService.createReceipt(receiptRequest);
//
//        // Assert
//        assertNotNull(createdReceipt);
//        assertEquals(1111111111111L, createdReceipt.getEmployeeId());
//        assertEquals(1L, createdReceipt.getSupplierId());
//    }

    private OrderRequest createOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setName("Nguyen Van A");
        orderRequest.setPhone("0123456789");
        orderRequest.setAddress("Ha Noi");
        orderRequest.setPaymentMethod(0);
        orderRequest.setPaymentStatus(false);
        orderRequest.setOrderType(true);
        orderRequest.setOrderStatus(0);
        orderRequest.setNote("Giao hàng nhanh");

        setProductDetailsId();

        OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
        orderDetailsRequest.setProductDetailsId(1L);
        orderDetailsRequest.setPrice(100000L);
        orderDetailsRequest.setQuantity(1);

        orderRequest.setOrderDetails(Collections.singletonList(orderDetailsRequest));

        return orderRequest;
    }

    // set productDetailsId = 55L có số lượng sản phẩm còn lại
    private void setProductDetailsId() {
        Optional<ProductDetails> productDetails = productDetailsRepository.findById(1L);
        productDetails.get().setQuantity(10);
        productDetailsRepository.save(productDetails.get());
    }

    @Test
    void create() {
        setUserDefaultLogin();
        OrderRequest orderRequest = createOrderRequest();
        OrderResponse orderResponse = orderService.create(orderRequest);

        // Assert
        // Kiểm tra xem có tạo thành công không
        assertNotNull(orderResponse);

    }

    @Test
    void update() {
        setUserDefaultLogin();
        OrderRequest orderRequest = createOrderRequest();
        OrderResponse orderResponse = orderService.create(orderRequest);

        int orderStatusOld = orderResponse.getOrderStatus(); // vừa tạo xong nên orderStatus = 0

        int orderStatusNew = 1;

        OrderResponse orderResponse2 = orderService.update(1L, orderStatusNew, "");

        // Assert
        // Kiểm tra xem có update thành công không
        assertNotNull(orderResponse2);
        assertEquals(0, orderStatusOld);
        assertEquals(1, orderResponse2.getOrderStatus());
    }

//    // thất bại khi
//            if (Objects.nonNull(order.getCompletedDate()))
//                    throw new RuntimeException(ORDER_COMPLETED_CANNOT_UPDATE);
//        if (order.getOrderStatus() == 4)
//                throw new RuntimeException(ORDER_CANCELLED_CANNOT_UPDATE);
//        if (order.getOrderStatus() == 5)
//                throw new RuntimeException(ORDER_RETURNED_CANNOT_UPDATE);
}