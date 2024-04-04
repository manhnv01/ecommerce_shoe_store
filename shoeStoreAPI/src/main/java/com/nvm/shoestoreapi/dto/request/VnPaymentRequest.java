package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VnPaymentRequest {
    public Integer trxTransactionId;

    public String fullname;

    public Integer amount;

    public Long orderInfo;

    public Date createDate;
}
