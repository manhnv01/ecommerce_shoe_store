package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailsResponse {
    private Long id;
    private Integer quantity;
    private Long price;
    private ProductDetailsResponse productDetails;
    private Long productId;
    private String productName;
    private String productColor;
    private String productSize;
    private String productThumbnail;
}
