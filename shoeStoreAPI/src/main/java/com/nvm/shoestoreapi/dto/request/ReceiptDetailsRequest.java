package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetailsRequest {
    private Long receiptId;
    private int quantity;
    private Long price;
    private Long productDetailsId;
}
