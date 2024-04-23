package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String phone;
    @Column
    private Boolean orderType; // Loại đơn hàng (Mua tại cửa hàng (false), mua online(true))
    @Column
    private Date paymentDate; // ngày thanh toán đơn hàng
    @Column(nullable = false)
    private Integer paymentMethod; // phương thức thanh toán (0: tiền mặt, 1: VNPAY)
    @Column(nullable = false)
    private Boolean paymentStatus; // trạng thái thanh toán (0: chưa thanh toán, 1: đã thanh toán)
    @Column
    private String note; // ghi chú
    @Column(nullable = false)
    private Integer orderStatus; // trạng thái đơn hàng
    @Column
    private Long transactionId; // mã giao dịch


    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate; // ngày tạo đơn hàng
    @Column
    private Date confirmDate; // ngày xác nhận đơn hàng
    @Column
    private Date deliveryDate; // ngày giao hàng cho đơn vị vận chuyển
    @Column
    private Date completedDate; // ngày hoàn thành đơn hàng
    @Column
    private Date returnDate; // ngày tra hàng
    @Column
    private Date cancelDate; // ngày hủy đơn hàng



    @Column
    private String cancelReason; // lý do hủy đơn hàng
    @Column
    private String carrier_name; // tên đơn vị vận chuyển
    @Column
    private String carrier_logo; // logo đơn vị vận chuyển
    @Column
    private String service; // dịch vụ vận chuyển
    @Column
    private Long total_fee; // phí vận chuyển
    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
