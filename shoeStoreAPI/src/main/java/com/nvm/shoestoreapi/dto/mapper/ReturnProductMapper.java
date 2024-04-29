package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.response.*;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ReturnProductMapper {
    final ModelMapper modelMapper;
    final ReturnProductRepository returnProductRepository;
    final ReturnProductDetailsRepository returnProductDetailsRepository;
    final ProductDetailsRepository productDetailsRepository;
    final OrderRepository orderRepository;
    final OrderDetailsRepository orderDetailsRepository;

    public ReturnProductResponse convertToResponse(ReturnProduct returnProduct) {
        ReturnProductResponse response = modelMapper.map(returnProduct, ReturnProductResponse.class);
        response.setCreatedAt(returnProduct.getCreatedAt());
        response.setUpdatedAt(returnProduct.getUpdatedAt());
        response.setStatus(returnProduct.isStatus());

        response.setOrderId(returnProduct.getOrder().getId());
        response.setOrderCreatedDate(returnProduct.getOrder().getCreatedDate());
        response.setOrderCompletedDate(returnProduct.getOrder().getCompletedDate());

        response.setEmployeeId(returnProduct.getOrder().getEmployee().getId());
        response.setEmployeeName(returnProduct.getOrder().getEmployee().getName());
        response.setCustomerId(returnProduct.getOrder().getCustomer().getId());
        response.setCustomerName(returnProduct.getOrder().getCustomer().getName());

        // tiính toánh tổng tiền trả lại
        long total = 0;
        for (ReturnProductDetails returnProductDetails : returnProduct.getReturnProductDetails()) {
            if (!returnProductDetails.isReturnType()) {
                continue;
            }

            OrderDetails orderDetails = orderDetailsRepository.findByOrderIdAndProductDetailsId(returnProduct.getOrder().getId(), returnProductDetails.getProductDetails().getId());
            // neu orderDetails sale thi tinh gia sale
            if (orderDetails.getSalePrice() != null) {
                total += orderDetails.getSalePrice() * returnProductDetails.getQuantity();
            } else {
                total += orderDetails.getPrice() * returnProductDetails.getQuantity();
            }
        }

        for (ReturnProductDetails returnProductDetails : returnProduct.getReturnProductDetails()) {
            // kiểm tra loai doi tra la tra hoặc đổi neu co tra thì cọng them phi van chuyen neu khong thì khong cộng
            if (returnProductDetails.isReturnType()) {
                total += returnProduct.getOrder().getTotal_fee();
                break;
            }
        }
        response.setTotalMoneyReturned(total);

        List<ReturnProductDetailsResponse> returnProductDetails = returnProduct.getReturnProductDetails().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return response;
    }

    public ReturnProductDetailsResponse convertToResponse(ReturnProductDetails returnProductDetails) {
        ReturnProductDetailsResponse response = modelMapper.map(returnProductDetails, ReturnProductDetailsResponse.class);
        response.setId(returnProductDetails.getId());

        response.setProductDetailsId(returnProductDetails.getProductDetails().getId());
        response.setProductName(returnProductDetails.getProductDetails().getProductColor().getProduct().getName());
        response.setProductColor(returnProductDetails.getProductDetails().getProductColor().getColor());
        response.setProductSize(returnProductDetails.getProductDetails().getSize());

        Long orderId = returnProductDetails.getReturnProduct().getOrder().getId();
        OrderDetails orderDetails = orderDetailsRepository.findByOrderIdAndProductDetailsId(orderId, returnProductDetails.getProductDetails().getId());

        response.setQuantitySold(orderDetails.getQuantity());
        response.setPriceSold(orderDetails.getPrice());

        response.setQuantityReturned(returnProductDetails.getQuantity());
        response.setReason(returnProductDetails.getReason());

        return response;
    }
}
