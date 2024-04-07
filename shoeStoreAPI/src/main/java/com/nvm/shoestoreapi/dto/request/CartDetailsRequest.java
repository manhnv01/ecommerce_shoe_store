package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

import static com.nvm.shoestoreapi.util.Constant.QUANTITY_MUST_BE_GREATER_THAN_0;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailsRequest {
    private Long id;
    @Min(value = 1, message = QUANTITY_MUST_BE_GREATER_THAN_0)
    private Integer quantity;
    private Long productDetailsId;
}
