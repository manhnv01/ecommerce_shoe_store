package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.OrderMapper;
import com.nvm.shoestoreapi.dto.request.ChangePasswordRequest;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.request.ResetPasswordRequest;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.entity.Account;
import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.entity.OrderDetails;
import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.repository.AccountRepository;
import com.nvm.shoestoreapi.repository.OrderDetailsRepository;
import com.nvm.shoestoreapi.repository.OrderRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.service.AccountService;
import com.nvm.shoestoreapi.service.OrderService;
import com.nvm.shoestoreapi.service.StorageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private StorageService storageService;
    @Autowired
    private VNPayService vnPayService;


    @Override
    public OrderResponse findById(Long id) {
        // kiem tra xem nguoi dung co quyen xem order nay khong
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (authority.getAuthority().equals(ROLE_ADMIN)
                    || authority.getAuthority().equals(ROLE_EMPLOYEE)
                    || order.getCustomer().getAccount().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
                return orderMapper.convertToResponse(order);
            else
                throw new RuntimeException(ORDER_NOT_FOUND);
        }
        return null;
    }

    @Override
    public OrderResponse create(OrderRequest orderRequest) {
        orderRequest.getOrderDetails().forEach(orderDetailsRequest -> {
            ProductDetails productDetails = productDetailsRepository
                    .findById(orderDetailsRequest.getProductDetailsId())
                    .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));
            if (productDetails.getQuantity() < orderDetailsRequest.getQuantity())
                throw new RuntimeException(PRODUCT_QUANTITY_NOT_ENOUGH);
        });
        Order order = orderMapper.convertToEntity(orderRequest);
        if (order.getOrderStatus() == 5) {
            Date now = new Date();
            order.setCompletedDate(now);
            if (!order.getPaymentStatus()) order.setPaymentStatus(true);
            if (order.getPaymentDate() == null) order.setPaymentDate(now);
            if (order.getConfirmDate() == null) order.setConfirmDate(now);
            if (order.getDeliveryToShipperDate() == null) order.setDeliveryToShipperDate(now);
            if (order.getDeliveryDate() == null) order.setDeliveryDate(now);
            if (order.getReceiveDate() == null) order.setReceiveDate(now);
        }
        orderRepository.save(order);
        order.getOrderDetails().clear();
        orderRequest.getOrderDetails().forEach(orderDetailsRequest -> {
            OrderDetails orderDetails = orderMapper.convertToEntity(orderDetailsRequest);
            orderDetails.setOrder(order);
            orderDetailsRepository.save(orderDetails);
            order.getOrderDetails().add(orderDetails);
        });

        // Cap nhat lai so luong san pham
        order.getOrderDetails().forEach(orderDetails -> {
            orderDetails.getProductDetails().setQuantity(orderDetails.getProductDetails().getQuantity() - orderDetails.getQuantity());
            productDetailsRepository.save(orderDetails.getProductDetails());
        });
        return orderMapper.convertToResponse(order);
    }

    @Override
    public OrderResponse update(Long id, Integer orderStatus, String cancelReason) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
        if (Objects.nonNull(order.getCompletedDate()))
            throw new RuntimeException("Đơn hàng đã hoàn thành không thể cập nhật");
        if (order.getOrderStatus() == 6)
            throw new RuntimeException("Đơn hàng đã hủy không thể cập nhật");

        order.setOrderStatus(orderStatus);
        Date now = new Date();

        if (order.getOrderStatus() == 1) {
            // don hang da duoc xac nhan
            order.setConfirmDate(now);
        }

        if (order.getOrderStatus() == 2) {
            // don hang da duoc giao cho shipper
            order.setDeliveryToShipperDate(now); // ngay giao cho shipper
            if (order.getConfirmDate() == null) order.setConfirmDate(now);
        }

        if (order.getOrderStatus() == 3) {
            // don hang da giao cho khach hang (xac nhan giao hang)
            order.setDeliveryDate(now); // ngay giao hang
            if (order.getConfirmDate() == null) order.setConfirmDate(now);
            if (order.getDeliveryToShipperDate() == null) order.setDeliveryToShipperDate(now);
        }

        if (order.getOrderStatus() == 4) {
            order.setReceiveDate(now);
            if (!order.getPaymentStatus()) order.setPaymentStatus(true);
            if (order.getPaymentDate() == null) order.setPaymentDate(now);
            if (order.getConfirmDate() == null) order.setConfirmDate(now);
            if (order.getDeliveryToShipperDate() == null) order.setDeliveryToShipperDate(now);
            if (order.getDeliveryDate() == null) order.setDeliveryDate(now);
        }

        if (order.getOrderStatus() == 5) {
            order.setCompletedDate(now);
            if (!order.getPaymentStatus()) order.setPaymentStatus(true);
            if (order.getPaymentDate() == null) order.setPaymentDate(now);
            if (order.getConfirmDate() == null) order.setConfirmDate(now);
            if (order.getDeliveryToShipperDate() == null) order.setDeliveryToShipperDate(now);
            if (order.getDeliveryDate() == null) order.setDeliveryDate(now);
            if (order.getReceiveDate() == null) order.setReceiveDate(now);
        }

        if (order.getOrderStatus() == 6) {
            order.setCancelDate(now);
            order.setCancelReason(cancelReason);
            if (order.getPaymentStatus()) {
                order.setPaymentStatus(false);
                order.setPaymentDate(null);
            }
            order.setCompletedDate(null);
            order.setReceiveDate(null);
            order.setDeliveryDate(null);
            order.setDeliveryToShipperDate(null);
            order.setConfirmDate(null);

            // Cap nhat lai so luong san pham
            order.getOrderDetails().forEach(orderDetails -> {
                orderDetails.getProductDetails().setQuantity(orderDetails.getProductDetails().getQuantity() + orderDetails.getQuantity());
                productDetailsRepository.save(orderDetails.getProductDetails());
            });
        }

        return orderMapper.convertToResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse updatePaymentStatus(Long id, Boolean paymentStatus, Date paymentTime) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
        order.setPaymentStatus(paymentStatus);
        if (paymentStatus) {
            order.setPaymentDate(paymentTime);
        }
        return orderMapper.convertToResponse(orderRepository.save(order));
    }

    @Override
    public Page<OrderResponse> findByCustomerAccountEmail(String email, Pageable pageable) {
        return orderRepository.findByCustomer_Account_Email(email, pageable).map(orderMapper::convertToResponse);
    }

    @Override
    public Page<OrderResponse> findByCustomerAccountEmailAndOrderStatus(String email, Integer orderStatus, Pageable pageable) {
        return orderRepository.findByCustomer_Account_EmailAndOrderStatus(email, orderStatus, pageable).map(orderMapper::convertToResponse);
    }

    @Override
    public long countByCustomerAccountEmailAndOrderStatus(String email, Integer orderStatus) {
        return orderRepository.countByCustomer_Account_EmailAndOrderStatus(email, orderStatus);
    }

    @Override
    public long countByCustomerAccountEmail(String email) {
        return orderRepository.countByCustomer_Account_Email(email);
    }

    @Override
    public Page<OrderResponse> findByOrderStatus(Integer orderStatus, Pageable pageable) {
        return orderRepository.findByOrderStatus(orderStatus, pageable).map(orderMapper::convertToResponse);
    }

    @Override
    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::convertToResponse);
    }

    @Override
    public long countByOrderStatus(Integer orderStatus) {
        return orderRepository.countByOrderStatus(orderStatus);
    }
}
