package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ReturnProductMapper;
import com.nvm.shoestoreapi.dto.request.ReturnProductDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.dto.response.ReturnProductResponse;
import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.entity.OrderDetails;
import com.nvm.shoestoreapi.entity.ReturnProduct;
import com.nvm.shoestoreapi.entity.ReturnProductDetails;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.ReturnProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class ReturnProductServiceImpl implements ReturnProductService {
    @Autowired
    private ReturnProductRepository returnProductRepository;
    @Autowired
    private ReturnProductDetailsRepository returnProductDetailsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReturnProductMapper returnProductMapper;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<ReturnProductResponse> findAll(Pageable pageable) {
        return returnProductRepository.findAll(pageable).map(returnProductMapper::convertToResponse);
    }

    @Override
    public ReturnProductResponse create(ReturnProductRequest returnProductRequest) {
        Order order = orderRepository.findById(returnProductRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));

        // kiểm tra xem order đã hoàn thành chưa
        if (order.getOrderStatus() != 3) {
            throw new RuntimeException(ORDER_NOT_COMPLETED);
        }

        for (ReturnProductDetailsRequest returnProductDetailsRequest : returnProductRequest.getReturnProductDetails()) {
            boolean isProductReturned = returnProductRepository.existsByOrderIdAndReturnProductDetails_ProductDetailsIdAndReturnProductDetails_ReturnTypeIsTrue(order.getId(), returnProductDetailsRequest.getProductDetailsId());
            boolean isProductExistsInOrder = false;
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                if (orderDetails.getProductDetails().getId().equals(returnProductDetailsRequest.getProductDetailsId())) {
                    isProductExistsInOrder = true;
                    if (isProductReturned) {
                        // Kiểm tra số lượng trả trước đó
                        int quantityReturned = returnProductDetailsRepository.findByReturnProduct_OrderIdAndProductDetailsIdAndReturnTypeIsTrue(order.getId(), returnProductDetailsRequest.getProductDetailsId())
                                .stream()
                                .mapToInt(ReturnProductDetails::getQuantity)
                                .sum();
                        if (returnProductDetailsRequest.getQuantity() + quantityReturned > orderDetails.getQuantity()) {
                            throw new RuntimeException(QUANTITY_RETURN_PRODUCT_MUST_BE_LESS_THAN_QUANTITY_ORDER);
                        }
                    } else {
                        if (returnProductDetailsRequest.getQuantity() > orderDetails.getQuantity()) {
                            throw new RuntimeException(QUANTITY_RETURN_PRODUCT_MUST_BE_LESS_THAN_QUANTITY_ORDER);
                        }
                    }
                    break;
                }
            }
            if (!isProductExistsInOrder) {
                throw new RuntimeException(RETURN_PRODUCT_NOT_BELONG_ORDER);
            }
        }


        ReturnProduct returnProduct = modelMapper.map(returnProductRequest, ReturnProduct.class);
        returnProduct.setOrder(order);
        returnProductRepository.save(returnProduct);

        // lay tai khoan dang dang nhap de lay ra employee
        returnProduct.setEmployee(employeeRepository.findByAccount_Email(SecurityContextHolder.getContext().getAuthentication().getName()));

        returnProduct.getReturnProductDetails().clear();

        // lay ra returnProductDetailsRequest de tao ra returnProductDetails va luu vao db
        for (ReturnProductDetailsRequest returnProductDetailsRequest : returnProductRequest.getReturnProductDetails()) {
            ReturnProductDetails returnProductDetails = new ReturnProductDetails();
            returnProductDetails.setReturnProduct(returnProduct);
            returnProductDetails.setProductDetails(productDetailsRepository.findById(returnProductDetailsRequest.getProductDetailsId())
                    .orElseThrow(() -> new RuntimeException(PRODUCT_DETAILS_NOT_FOUND)));
            returnProductDetails.setQuantity(returnProductDetailsRequest.getQuantity());
            returnProductDetails.setReturnProduct(returnProduct);
            returnProductDetails.setReturnType(returnProductDetailsRequest.isReturnType());
            returnProductDetailsRepository.save(returnProductDetails);

            // cap nhat lai so luong san pham trong kho nếu trả hàng
            if (returnProductDetailsRequest.isReturnType()){
                returnProductDetails.getProductDetails().setQuantity(returnProductDetails.getProductDetails().getQuantity() + returnProductDetailsRequest.getQuantity());
                productDetailsRepository.save(returnProductDetails.getProductDetails());
            }
            returnProduct.getReturnProductDetails().add(returnProductDetails);
        }

        return returnProductMapper.convertToResponse(returnProduct);
    }

    @Override
    public ReturnProductResponse update(ReturnProductRequest ReturnProductRequest) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public long count() {
        return returnProductRepository.count();
    }

    @Override
    public Optional<ReturnProductResponse> findById(Long id) {
        return Optional.empty();
    }
}