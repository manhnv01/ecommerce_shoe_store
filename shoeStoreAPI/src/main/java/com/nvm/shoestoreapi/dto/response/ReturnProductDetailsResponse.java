package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductDetailsResponse {
    private Long id;

    private Long productDetailsId;
    private String productName;
    private String productColor;
    private String productSize;
    private int quantitySold;
    private Long priceSold;

    private int quantityReturned;
    private String reason;
}
