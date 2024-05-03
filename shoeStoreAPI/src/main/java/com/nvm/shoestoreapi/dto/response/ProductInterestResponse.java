package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInterestResponse {
    private Long productId;
    private String productName;
    private String productThumbnail;
    private String productColor;
    private String productSize;
    private Integer productQuantity;
    private Long quantityInterest;
}
