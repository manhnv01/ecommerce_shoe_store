package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponse {
    private Long id;
    private Long productDetailsId;
    private String productSize;
    private String productSlug;
    private String productThumbnail;
    private String productName;
    private String productColor;
    private Long productPrice; // giá gốc
    private Integer quantity; // số lượng sản phẩm
    private Long salePrice; // giá khuyến mãi
    private Long totalPrice; // thành tiền
}
