package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ReturnProductMapper;
import com.nvm.shoestoreapi.dto.request.ReturnProductDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReturnProductRequest;
import com.nvm.shoestoreapi.dto.response.ReturnProductResponse;
import com.nvm.shoestoreapi.entity.Order;
import com.nvm.shoestoreapi.entity.OrderDetails;
import com.nvm.shoestoreapi.entity.ReturnProduct;
import com.nvm.shoestoreapi.entity.ReturnProductDetails;
import com.nvm.shoestoreapi.repository.OrderRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.repository.ReturnProductDetailsRepository;
import com.nvm.shoestoreapi.repository.ReturnProductRepository;
import com.nvm.shoestoreapi.service.ReturnProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<ReturnProductResponse> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public ReturnProductResponse create(ReturnProductRequest returnProductRequest) {
        Order order = orderRepository.findById(returnProductRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND));
        for (ReturnProductDetailsRequest returnProductDetailsRequest : returnProductRequest.getReturnProductDetails()) {
            for (OrderDetails orderDetails : order.getOrderDetails()) {
                if (orderDetails.getProductDetails().getId().equals(returnProductDetailsRequest.getProductDetailsId())) {
                    if (returnProductDetailsRequest.getQuantity() > orderDetails.getQuantity()) {
                        throw new RuntimeException(QUANTITY_RETURN_PRODUCT_MUST_BE_LESS_THAN_QUANTITY_ORDER);
                    }
                } else {
                    throw new RuntimeException(RETURN_PRODUCT_NOT_BELONG_ORDER);
                }
            }
        }
        ReturnProduct returnProduct = new ReturnProduct();
        returnProduct.setStatus(false);
        returnProduct.setOrder(order);
        ReturnProduct returnProductSaved = returnProductRepository.save(returnProduct);

        // lay ra returnProductDetailsRequest de tao ra returnProductDetails va luu vao db
        for (ReturnProductDetailsRequest returnProductDetailsRequest : returnProductRequest.getReturnProductDetails()) {
            ReturnProductDetails returnProductDetails = new ReturnProductDetails();
            returnProductDetails.setReturnProduct(returnProductSaved);
            returnProductDetails.setProductDetails(productDetailsRepository.findById(returnProductDetailsRequest.getProductDetailsId())
                    .orElseThrow(() -> new RuntimeException(PRODUCT_DETAILS_NOT_FOUND)));
            returnProductDetails.setQuantity(returnProductDetailsRequest.getQuantity());
            returnProductDetailsRepository.save(returnProductDetails);
        }

        return returnProductMapper.convertToResponse(returnProductSaved);
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
