package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.BrandRepository;
import com.nvm.shoestoreapi.repository.ProductDetailsRepository;
import com.nvm.shoestoreapi.repository.ReceiptDetailsRepository;
import com.nvm.shoestoreapi.repository.ReceiptRepository;
import com.nvm.shoestoreapi.service.BrandService;
import com.nvm.shoestoreapi.service.ReceiptService;
import com.nvm.shoestoreapi.service.StorageService;
import com.nvm.shoestoreapi.util.SlugUtil;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ReceiptDetailsRepository receiptDetailsRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();

    @Override
    public List<Receipt> findAll() {
        return receiptRepository.findAll();
    }

    @Override
    public Receipt createReceipt(ReceiptRequest receiptRequest) {
        Receipt receipt = modelMapper.map(receiptRequest, Receipt.class);
        receipt.setCreatedDate(new Date());

        Receipt savedReceipt = receiptRepository.save(receipt);

        List<ReceiptDetails> receiptDetailsList = receiptRequest.getReceiptDetails().stream()
                .map(receiptDetailsRequest -> mapToReceiptDetails(receiptDetailsRequest, savedReceipt))
                .collect(Collectors.toList());

        receiptDetailsRepository.saveAll(receiptDetailsList);

        return savedReceipt;
    }
    private ReceiptDetails mapToReceiptDetails(ReceiptDetailsRequest receiptDetailsRequest, Receipt receipt) {
        ReceiptDetails receiptDetails = modelMapper.map(receiptDetailsRequest, ReceiptDetails.class);

        ProductDetails productDetails = productDetailsRepository.findById(receiptDetailsRequest.getProductDetailsId())
                .orElseThrow(() -> new RuntimeException("ProductDetails not found with id: " + receiptDetailsRequest.getProductDetailsId()));

        // Update the quantity in ProductDetails
        int newQuantity = productDetails.getQuantity() + receiptDetailsRequest.getQuantity();
        productDetails.setQuantity(newQuantity);

        // Save the updated ProductDetails
        productDetailsRepository.save(productDetails);

        receiptDetails.setProductDetails(productDetails);
        receiptDetails.setReceipt(receipt);
        return receiptDetails;
    }
}
