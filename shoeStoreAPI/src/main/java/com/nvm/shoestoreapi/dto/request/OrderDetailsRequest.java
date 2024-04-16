package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

import static com.nvm.shoestoreapi.util.Constant.*;
import static com.nvm.shoestoreapi.util.Constant.QUANTITY_MAX_1000;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsRequest {
    private Long productDetailsId;

    @Min(value = 0, message = PRICE_MIN_0)
    @Max(value = 1000000000, message = PRICE_MAX_1_BILLION)
    private Long price;

    @Min(value = 1, message = QUANTITY_MUST_BE_GREATER_THAN_0)
    @Max(value = 1000, message = QUANTITY_MAX_1000)
    private int quantity;
}
