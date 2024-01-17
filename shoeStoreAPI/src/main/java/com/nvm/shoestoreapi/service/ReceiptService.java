package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Receipt;

import java.util.List;

public interface ReceiptService {
    List<Receipt> findAll();
    Receipt createReceipt (ReceiptRequest receiptRequest);
}
