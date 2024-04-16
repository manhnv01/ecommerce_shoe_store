package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 30, message = NAME_MAX_LENGTH_30)
    private String name;
    @NotBlank(message = ADDRESS_NOT_BLANK)
    @Length(max = 100, message = ADDRESS_MAX_LENGTH_100)
    private String address;
    @Length(min = 10, max = 10, message = PHONE_NUMBER_MUST_HAVE_10_DIGITS)
    private String phone;
    @NotNull(message = ORDER_TYPE_NOT_BLANK)
    private Boolean orderType; // Loại đơn hàng (Mua tại cửa hàng, mua online)
    private Date paymentDate; // ngày thanh toán đơn hàng
    @NotNull(message = PAYMENT_METHOD_NOT_BLANK)
    private Integer paymentMethod; // phương thức thanh toán (0: tiền mặt, 1: VNPAY)
    @NotNull(message = PAYMENT_STATUS_NOT_BLANK)
    private Boolean paymentStatus; // trạng thái thanh toán (0: chưa thanh toán, 1: đã thanh toán)
    private Date completedDate; // ngày hoàn thành đơn hàng
    private String note; // ghi chú
    @NotNull(message = ORDER_STATUS_NOT_BLANK)
    private Integer orderStatus;
    private String cancelReason; // lý do hủy đơn hàng
    @NotEmpty(message = PRODUCTS_NOT_EMPTY)
    @Valid
    private List<OrderDetailsRequest> orderDetails; // danh sách sản phẩm
}
