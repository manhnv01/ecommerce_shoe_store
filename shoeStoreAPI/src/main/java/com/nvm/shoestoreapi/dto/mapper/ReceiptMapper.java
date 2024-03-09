package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.request.ReceiptDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.response.ReceiptDetailsResponse;
import com.nvm.shoestoreapi.dto.response.ReceiptResponse;
import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.entity.ReceiptDetails;
import com.nvm.shoestoreapi.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ReceiptMapper {
    final ModelMapper modelMapper;
    final ReceiptDetailsRepository receiptDetailsRepository;
    final ReceiptRepository receiptRepository;
    final SupplierRepository supplierRepository;
    final EmployeeRepository employeeRepository;
    final ProductDetailsRepository productDetailsRepository;

    // ReceiptMapper
    public ReceiptResponse convertToResponse(Receipt receipt) {
        ReceiptResponse receiptResponse = modelMapper.map(receipt, ReceiptResponse.class);
        receiptResponse.setEmployeeName(receipt.getEmployee().getName());
        receiptResponse.setSupplierId(receipt.getSupplier().getId());
        receiptResponse.setEmployeeId(receipt.getEmployee().getId());
        receiptResponse.setCreatedAt(receipt.getCreatedAt());
        receiptResponse.setUpdatedAt(receipt.getUpdatedAt());
        receiptResponse.setSupplierName(receipt.getSupplier().getName());
        receiptResponse.setTotalMoney(receipt.getReceiptDetails().stream().mapToLong(receiptDetails -> receiptDetails.getQuantity() * receiptDetails.getPrice()).sum());
        receiptResponse.setReceiptDetails(receipt.getReceiptDetails().stream().map(receiptDetails -> {
            ReceiptDetailsResponse receiptDetailsResponse = modelMapper.map(receiptDetails, ReceiptDetailsResponse.class);
            receiptDetailsResponse.setProductName(receiptDetails.getProductDetails().getProductColor().getProduct().getName());
            receiptDetailsResponse.setProductColor(receiptDetails.getProductDetails().getProductColor().getColor());
            receiptDetailsResponse.setProductId(receiptDetails.getProductDetails().getProductColor().getProduct().getId());
            return receiptDetailsResponse;
        }).collect(Collectors.toList()));
        return receiptResponse;
    }

    public Receipt convertToEntity(ReceiptRequest receiptRequest) {
        Receipt receipt = modelMapper.map(receiptRequest, Receipt.class);
        receipt.setSupplier(supplierRepository.findById(receiptRequest.getSupplierId()).orElse(null));
        //receipt.setEmployee(employeeRepository.findByAccountEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null));
        receipt.setEmployee(employeeRepository.findById(1709806271419L).orElse(null));
        return receipt;
    }

    public ReceiptDetails convertToEntity(ReceiptDetailsRequest receiptDetailsRequest) {
        ReceiptDetails receiptDetails = modelMapper.map(receiptDetailsRequest, ReceiptDetails.class);
        receiptDetails.setProductDetails(productDetailsRepository.findById(receiptDetailsRequest.getProductDetailsId()).orElse(null));
        return receiptDetails;
    }
}
