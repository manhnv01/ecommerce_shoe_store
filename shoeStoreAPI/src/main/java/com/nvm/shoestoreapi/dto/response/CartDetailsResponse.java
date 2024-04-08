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
    private Integer productQuantity; // tổng số lượng sản phẩm này trong kho
    private Integer quantity; // số lượng sản phẩm trong giỏ hàng
    private Long salePrice; // giá sau khi sale
    private Long totalPrice; // tổng tiền của sản phẩm
    private Long totalSalePrice; // tổng tiền sau khi sale
}
