package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptReport {
    private Integer month; // Tháng
    private Integer year; // Năm
    private Integer totalQuantityReceipt; // Tổng số lượng nhập
    private Double totalMoneyReceipt; // Tổng tiền nhập
    private List<ProductReport> products; // Danh sách sản phẩm
}
