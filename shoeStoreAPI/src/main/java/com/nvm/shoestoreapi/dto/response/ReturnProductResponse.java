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
    private boolean status;

    private Long orderId;
    private Date orderCreatedDate;
    private Date orderCompletedDate;

    private Long employeeId;
    private String employeeName;

    private Long customerId;
    private String customerName;

    private List<ReturnProductDetailsResponse> returnProductDetails;
}
