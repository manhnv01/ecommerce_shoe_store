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
public class ProductReport {
    private Long id;
    private String name;
    private String color;
    private Integer totalQuantity; // Số lượng đã bán / đã nhập / tồn kho
    private Long totalMoney; // Tổng tiền đã bán / đã nhập
}
