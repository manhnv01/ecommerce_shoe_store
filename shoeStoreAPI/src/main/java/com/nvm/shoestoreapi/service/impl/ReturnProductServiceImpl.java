package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ReturnProductMapper;
import com.nvm.shoestoreapi.dto.request.ReturnProductDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.dto.response.ReturnProductResponse;
import com.nvm.shoestoreapi.entity.*;
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
            boolean isProductReturned = returnProductRepository.existsByOrderIdAndStatusAndReturnProductDetails_ProductDetailsIdAndReturnProductDetails_ReturnTypeIsTrue(order.getId(), RETURN_APPROVED, returnProductDetailsRequest.getProductDetailsId());
            boolean isProductExistsInOrder = false;
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                if (orderDetails.getProductDetails().getId().equals(returnProductDetailsRequest.getProductDetailsId())) {
                    isProductExistsInOrder = true;
                    if (isProductReturned) {
                        // Kiểm tra số lượng trả trước đó
                        int quantityReturned = returnProductDetailsRepository.findByReturnProduct_OrderIdAndReturnProduct_StatusAndProductDetailsIdAndReturnTypeIsTrue(order.getId(), RETURN_APPROVED, returnProductDetailsRequest.getProductDetailsId())
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
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeRepository.findByAccount_Email(userEmail);

        // Kiểm tra xem nhân viên có null hay không
        // Gán null cho returnProduct.setEmployee khi không tìm thấy nhân viên
        // Gán nhân viên cho returnProduct
        returnProduct.setEmployee(employee);


        returnProduct.getReturnProductDetails().clear();

        // lay ra returnProductDetailsRequest de tao ra returnProductDetails va luu vao db
        for (ReturnProductDetailsRequest returnProductDetailsRequest : returnProductRequest.getReturnProductDetails()) {
            ReturnProductDetails returnProductDetails = new ReturnProductDetails();
            returnProductDetails.setReturnProduct(returnProduct);
            returnProductDetails.setProductDetails(productDetailsRepository.findById(returnProductDetailsRequest.getProductDetailsId())
                    .orElseThrow(() -> new RuntimeException(PRODUCT_DETAILS_NOT_FOUND)));
            returnProductDetails.setQuantity(returnProductDetailsRequest.getQuantity());
            returnProductDetails.setReturnProduct(returnProduct);
            returnProductDetails.setReason(returnProductDetailsRequest.getReason());
            returnProductDetails.setReturnType(returnProductDetailsRequest.isReturnType());
            returnProductDetailsRepository.save(returnProductDetails);

            // cap nhat lai so luong san pham trong kho nếu trả hàng
            if (returnProductDetailsRequest.isReturnType() && returnProduct.getStatus().equals(RETURN_APPROVED)) {
                returnProductDetails.getProductDetails().setQuantity(returnProductDetails.getProductDetails().getQuantity() + returnProductDetailsRequest.getQuantity());
                productDetailsRepository.save(returnProductDetails.getProductDetails());
            }
            returnProduct.getReturnProductDetails().add(returnProductDetails);
        }

        return returnProductMapper.convertToResponse(returnProduct);
    }

    @Override
    public ReturnProductResponse update(Long id, String status, String reason) {
        ReturnProduct returnProduct = returnProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(RETURN_PRODUCT_NOT_FOUND));

        if (returnProduct.getStatus().equals(RETURN_APPROVED) || returnProduct.getStatus().equals(RETURN_REJECTED)) {
            throw new RuntimeException(RETURN_PRODUCT_STATUS_CANNOT_BE_CHANGED);
        }

        // neu nhan vien null thi set nhan vien dang dang nhap
        if (returnProduct.getEmployee() == null) {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Employee employee = employeeRepository.findByAccount_Email(userEmail);
            returnProduct.setEmployee(employee);
        }

        returnProduct.setStatus(status);
        returnProduct.setReason(reason);
        returnProductRepository.save(returnProduct);

        // cap nhat lai so luong san pham trong kho nếu trả hàng
        if (status.equals(RETURN_APPROVED)) {
            for (ReturnProductDetails returnProductDetails : returnProduct.getReturnProductDetails()) {
                if (returnProductDetails.isReturnType()) {
                    returnProductDetails.getProductDetails().setQuantity(returnProductDetails.getProductDetails().getQuantity() - returnProductDetails.getQuantity());
                    productDetailsRepository.save(returnProductDetails.getProductDetails());
                }
            }
        }

        return returnProductMapper.convertToResponse(returnProduct);
    }

    @Override
    public long count() {
        return returnProductRepository.count();
    }

    @Override
    public Optional<ReturnProductResponse> findById(Long id) {
        return returnProductRepository.findById(id).map(returnProductMapper::convertToResponse);
    }

    @Override
    public int countByStatus(String status) {
        return returnProductRepository.countByStatus(status);
    }

    @Override
    public Page<ReturnProductResponse> findByStatus(String status, Pageable pageable) {
        return returnProductRepository.findByStatus(status, pageable)
                .map(returnProductMapper::convertToResponse);
    }

    @Override
    public Page<ReturnProductResponse> findByEmployeeNameOrCustomerNameContaining(String employeeName, String customerName, Pageable pageable) {
        return returnProductRepository.findByEmployeeNameContainingOrOrder_Customer_NameContaining(
                        employeeName, customerName, pageable)
                .map(returnProductMapper::convertToResponse);
    }

    @Override
    public Page<ReturnProductResponse> findByCustomerAccountEmail(String email, Pageable pageable) {
        return returnProductRepository.findByOrder_Customer_Account_Email(email, pageable)
                .map(returnProductMapper::convertToResponse);
    }

    @Override
    public long countByCustomerAccountEmailAndStatus(String email, String status) {
        return returnProductRepository.countByOrder_Customer_Account_EmailAndStatus(email, status);
    }

    @Override
    public long countByCustomerAccountEmail(String email) {
        return returnProductRepository.countByOrder_Customer_Account_Email(email);
    }
}
