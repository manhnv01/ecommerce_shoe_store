package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.OrderMapper;
import com.nvm.shoestoreapi.dto.request.OrderRequest;
import com.nvm.shoestoreapi.dto.response.OrderResponse;
import com.nvm.shoestoreapi.dto.response.ReportBrandResponse;
import com.nvm.shoestoreapi.dto.response.ReportCategoryResponse;
import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.entity.OrderDetails;
import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.repository.EmployeeRepository;
import com.nvm.shoestoreapi.repository.OrderDetailsRepository;
import com.nvm.shoestoreapi.repository.OrderRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private EmployeeRepository employeeRepository;

    @Override
    public OrderResponse findById(Long id) {
        // kiem tra xem nguoi dung co quyen xem order nay khong
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
//        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
//            if (authority.getAuthority().equals(ROLE_ADMIN)
//                    || authority.getAuthority().equals(ROLE_EMPLOYEE)
//                    || order.getCustomer().getAccount().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
//                return orderMapper.convertToResponse(order);
//            else
//                throw new RuntimeException(FORBIDDEN);
//        }
        return orderMapper.convertToResponse(order);
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

        // Nếu là đơn hàng ta cửa hàng thì hoàn thành luôn
        if (!order.getOrderType()) {
            Date now = new Date();
            order.setOrderStatus(3);
            order.setCompletedDate(now);
            order.setPaymentStatus(true);
            order.setPaymentDate(now);
            order.setConfirmDate(now);
            order.setDeliveryDate(now);
            order.setReturnDate(now);
        }
        orderRepository.save(order);
        order.getOrderDetails().clear();
        orderRequest.getOrderDetails().forEach(orderDetailsRequest -> {
            OrderDetails orderDetails = orderMapper.convertToEntity(orderDetailsRequest);
            orderDetails.setOrder(order);
            orderDetailsRepository.save(orderDetails);
            order.getOrderDetails().add(orderDetails);
        });

        // Cập nhật lại số lượng sản phẩm trừ đi số lượng sản phẩm đã mua
        order.getOrderDetails().forEach(orderDetails -> {
            orderDetails.getProductDetails().setQuantity(orderDetails.getProductDetails().getQuantity() - orderDetails.getQuantity());
            productDetailsRepository.save(orderDetails.getProductDetails());
        });
        return orderMapper.convertToResponse(order);
    }

//            "0" Chờ xác nhận
//            "1" Đã xác nhận
//            "2" Đang giao hàng
//            "3" Đã giao
//            "4" Đã hủy
//            "5" Đã trả hàng

    @Override
    public OrderResponse update(Long id, Integer orderStatus, String cancelReason) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
        if (Objects.nonNull(order.getCompletedDate()))
            throw new RuntimeException(ORDER_COMPLETED_CANNOT_UPDATE);
        if (order.getOrderStatus() == 4)
            throw new RuntimeException(ORDER_CANCELLED_CANNOT_UPDATE);
        if (order.getOrderStatus() == 5)
            throw new RuntimeException(ORDER_RETURNED_CANNOT_UPDATE);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(ROLE_ADMIN) || authority.getAuthority().equals(ROLE_EMPLOYEE)) {
                if (order.getEmployee() == null) {
                    order.setEmployee(employeeRepository.findByAccount_Email(authentication.getName()));
                }
            }
        }
        order.setOrderStatus(orderStatus);
        Date now = new Date();

        switch (order.getOrderStatus()) {
            case 0: // Chờ xác nhận
                break;
            case 1: // Đã xác nhận
                order.setConfirmDate(now);
                break;
            case 2: // Đang giao hàng
                order.setDeliveryDate(now);
                if (order.getConfirmDate() == null) order.setConfirmDate(now);
                break;
            case 3: // Đã giao
                order.setCompletedDate(now);
                if (!order.getPaymentStatus()) order.setPaymentStatus(true);
                if (order.getPaymentDate() == null) order.setPaymentDate(now);
                if (order.getConfirmDate() == null) order.setConfirmDate(now);
                if (order.getDeliveryDate() == null) order.setDeliveryDate(now);
                if (order.getReturnDate() == null) order.setReturnDate(now);
                break;
            case 4: // Đã hủy
                order.setCancelDate(now);
                order.setCancelReason(cancelReason);
                order.getOrderDetails().forEach(orderDetails -> {
                    orderDetails.getProductDetails().setQuantity(orderDetails.getProductDetails().getQuantity() + orderDetails.getQuantity());
                    productDetailsRepository.save(orderDetails.getProductDetails());
                });
                break;
            case 5: // Đã trả hàng
                break;
            case 6: // Yêu cầu trả
                break;
        }

        return orderMapper.convertToResponse(orderRepository.save(order));
    }

    @Override
    public void updatePaymentStatus(Long id, Boolean paymentStatus, Date paymentTime, Long transactionId) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
        order.setPaymentStatus(paymentStatus);
        order.setTransactionId(transactionId);
        if (paymentStatus) {
            order.setPaymentDate(paymentTime);
        }
        orderRepository.save(order);
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

    @Override
    public long count() {
        return orderRepository.count();
    }

    @Override
    public List<OrderResponse> findByOrderStatus(Integer orderStatus) {
        return orderRepository.findByOrderStatus(orderStatus)
                .stream()
                .map(orderMapper::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public long countByCreatedDateToday() {
        return orderRepository.countByCreatedDateToday();
    }

    // để
    @Override
    public List<ReportCategoryResponse> getProductCountByCategory() {
        return orderDetailsRepository.getProductCountByCategory();
    }

    @Override
    public List<ReportBrandResponse> getProductCountByBrand() {
        return orderDetailsRepository.getProductCountByBrand();
    }
}
