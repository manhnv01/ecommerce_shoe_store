package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductDetailsRequest {
    private Long id;
    private int quantity;
    private String reason;
    private Long productDetailsId;
    private Long returnProductId;
}
