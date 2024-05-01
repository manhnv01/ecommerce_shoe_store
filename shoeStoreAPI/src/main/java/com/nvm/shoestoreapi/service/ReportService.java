package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.response.OrderReport;
import com.nvm.shoestoreapi.dto.response.ReceiptReport;
import com.nvm.shoestoreapi.dto.response.ReceiptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    List<OrderReport> getOrderReport(Integer year);

    List<ReceiptReport> getReceiptReport(Integer year);

    byte[] exportOrderReport(Integer year);

    byte[] exportReceiptReport(Integer year);
//
//    DashboardResponse getDashboard();
//
//    List<CartReport> getAllCart();
}
