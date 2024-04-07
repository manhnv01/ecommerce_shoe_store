package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private Date paymentDate; // ngày thanh toán đơn hàng
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private Integer paymentMethod; // phương thức thanh toán (0: tiền mặt, 1: VNPAY)
    @NotNull(message = "Trạng thái thanh toán không được để trống")
    private Boolean paymentStatus; // trạng thái thanh toán (0: chưa thanh toán, 1: đã thanh toán)
    private Date completedDate; // ngày hoàn thành đơn hàng
    private String note; // ghi chú
    @NotNull(message = "Trạng thái đơn hàng không được để trống")
    private Integer orderStatus; // trạng thái đơn hàng (0: chờ xác nhận, 1: đã xác nhận, 2: đang giao hàng, 3: đã giao hàng, 4: đã hủy)
    private String cancelReason; // lý do hủy đơn hàng
    @NotEmpty(message = "Danh sách sản phẩm không được để trống")
    @Valid
    private List<OrderDetailsRequest> orderDetails; // danh sách sản phẩm
}
