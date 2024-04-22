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
public class OrderResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private Date createdDate;
    private Date paymentDate;
    private Integer paymentMethod;
    private Boolean paymentStatus;
    private Date completedDate;
    private String note;
    private Integer orderStatus;
    private Date confirmDate; // ngày xác nhận đơn hàng
    private Date deliveryToShipperDate; // ngày giao cho shipper
    private Date deliveryDate; // ngày giao hàng
    private Date receiveDate; // ngày nhận hàng
    private Date cancelDate; // ngày hủy đơn hàng
    private String cancelReason; // lý do hủy đơn hàng

    private Long totalMoney; // tổng tiền đơn hàng
    private Long totalSaleMoney; // tổng tiền sau khi sale
    private Long totalDiscount; // tổng giảm giá
    private Long totalQuantity; // tổng số lượng sản phẩm trong đơn hàng

    // thông tin khách hàng
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerCreatedDate;
    private Long customerTotalOrder;
    private Long customerTotalMoney;

    // thông tin vận chuyển
    private String carrier_name; // tên đơn vị vận chuyển
    private String carrier_logo; // logo đơn vị vận chuyển
    private String service; // dịch vụ vận chuyển
    private Long total_fee; // phí vận chuyển

    private List<OrderDetailsResponse> orderDetails;
}
