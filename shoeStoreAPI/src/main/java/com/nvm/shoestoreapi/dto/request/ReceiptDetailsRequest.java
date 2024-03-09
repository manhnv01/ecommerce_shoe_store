package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailsRequest {
    private Long id;
    @Min(value = 1, message = QUANTITY_MUST_BE_GREATER_THAN_0)
    @Max(value = 1000, message = QUANTITY_MAX_1000)
    private int quantity;
    @Min(value = 0, message = PRICE_MIN_0)
    @Max(value = 1000000000, message = PRICE_MAX_1_BILLION)
    private Long price;
    private Long productDetailsId;
}
