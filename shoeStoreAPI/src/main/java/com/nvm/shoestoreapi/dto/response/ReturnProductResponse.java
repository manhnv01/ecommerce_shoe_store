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
public class ReturnProductResponse {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String reason;

    private Long orderId;
    private Long total_fee;
    private Date orderCreatedDate;
    private Date orderCompletedDate;

    private Long employeeId;
    private String employeeName;

    private Long customerId;
    private String customerName;

    private Long totalMoneyReturned;

    private List<ReturnProductDetailsResponse> returnProductDetails;
}
