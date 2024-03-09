package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ReceiptMapper;
import com.nvm.shoestoreapi.dto.request.ReceiptDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.response.ReceiptResponse;
import com.nvm.shoestoreapi.entity.ProductDetails;
import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.entity.ReceiptDetails;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.ReceiptService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ReceiptDetailsRepository receiptDetailsRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    @Autowired
    private ReceiptMapper receiptMapper;

    @Override
    public Page<ReceiptResponse> findAll(Pageable pageable) {
        return receiptRepository.findAll(pageable).map(receiptMapper::convertToResponse);
    }

    @Override
    public Page<ReceiptResponse> findByEmployeeNameOrSupplierNameContaining(String employeeName, String supplierName,Pageable pageable) {
        return receiptRepository.findByEmployeeNameContainingOrSupplierNameContaining(employeeName, supplierName, pageable).map(receiptMapper::convertToResponse);
    }

    @Override
    public long count() {
        return receiptRepository.count();
    }

    @Override
    public ReceiptResponse findByReceiptId(Long id) {
        return receiptMapper.convertToResponse(receiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(RECEIPT_NOT_FOUND)));
    }

    @Override
    public ReceiptResponse createReceipt(ReceiptRequest receiptRequest) {
        if (receiptRequest.getEmployeeId() == null) {
            throw new RuntimeException(EMPLOYEE_NOT_FOUND);
        }
        if (receiptRequest.getSupplierId() == null) {
            throw new RuntimeException(SUPPLIER_NOT_FOUND);
        }

        Receipt receipt = receiptMapper.convertToEntity(receiptRequest);
        Receipt savedReceipt = receiptRepository.save(receipt);

        List<ReceiptDetails> receiptDetailsList = receiptRequest.getReceiptDetails().stream()
                .map(receiptDetailsRequest -> mapToReceiptDetails(receiptDetailsRequest, receipt))
                .collect(Collectors.toList());

        receiptDetailsRepository.saveAll(receiptDetailsList);
        savedReceipt.setReceiptDetails(receiptDetailsList);

        return receiptMapper.convertToResponse(savedReceipt);
    }

    private ReceiptDetails mapToReceiptDetails(ReceiptDetailsRequest receiptDetailsRequest, Receipt receipt) {
        ReceiptDetails receiptDetails = receiptMapper.convertToEntity(receiptDetailsRequest);

        ProductDetails productDetails = productDetailsRepository.findById(receiptDetailsRequest.getProductDetailsId())
                .orElseThrow(() -> new RuntimeException(PRODUCT_DETAILS_NOT_FOUND));

        // Update the quantity in ProductDetails
        int newQuantity = productDetails.getQuantity() + receiptDetailsRequest.getQuantity();
        productDetails.setQuantity(newQuantity);
        productDetailsRepository.save(productDetails);

        receiptDetails.setProductDetails(productDetails);
        receiptDetails.setReceipt(receipt);
        return receiptDetails;
    }
}
