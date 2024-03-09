package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private Long employeeId;
    private String employeeName;
    private Long supplierId;
    private String supplierName;
    private Long totalMoney;
    private List<ReceiptDetailsResponse> receiptDetails;
}
