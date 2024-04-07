package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsRequest {
    @NotNull(message = "Mã sản phẩm không được để trống")
    private Long productDetailsId;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0", inclusive = false, message = "Giá phải lớn hơn 0")
    private Long price;

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @DecimalMin(value = "0", inclusive = false, message = "Số lượng phải lớn hơn 0")
    private int quantity;
}
