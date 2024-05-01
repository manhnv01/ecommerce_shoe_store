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
public class OrderReport {
    private Integer month; // Tháng
    private Integer year; // Năm
    private Integer totalQuantityOrder; // Tổng sản phẩm đã bán
    private Double totalMoneyOrder; // Tổng tiền bán
    private List<ProductReport> products; // Danh sách sản phẩm
}
