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
public class CartDetailsResponse {
    private Long id;
    private Long productDetailsId;
    private String productSize;
    private String productSlug;
    private String productThumbnail;
    private String productName;
    private String productColor;
    private Long productPrice;
    private Integer quantity;
    private Long totalQuantity;
    private Long salePrice;
    private Long totalPrice;
}
