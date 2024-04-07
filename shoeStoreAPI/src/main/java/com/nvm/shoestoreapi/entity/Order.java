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
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;
    @Column
    private Date paymentDate; // ngày thanh toán đơn hàng
    @Column(nullable = false)
    private Integer paymentMethod; // phương thức thanh toán (0: tiền mặt, 1: VNPAY)
    @Column(nullable = false)
    private Boolean paymentStatus; // trạng thái thanh toán (0: chưa thanh toán, 1: đã thanh toán)
    @Column
    private Date completedDate; // ngày hoàn thành đơn hàng
    @Column
    private String note; // ghi chú
    /*
     * 0: chờ xác nhận
     * 1: đã xác nhận
     * 2: đang giao hàng (đã giao cho đơn vị vận chuyển)
     * 3: đã giao hàng
     * 4: đã nhận hàng
     * 5: đã hoàn thành
     * 6: đã hủy
     */
    @Column(nullable = false)
    private Integer orderStatus; // trạng thái đơn hàng
    @Column
    private Date confirmDate; // ngày xác nhận đơn hàng
    @Column
    private Date deliveryToShipperDate; // ngày giao hàng cho đơn vị vận chuyển
    @Column
    private Date deliveryDate; // ngày giao hàng
    @Column
    private Date receiveDate; // ngày nhận hàng
    @Column
    private Date cancelDate; // ngày hủy đơn hàng
    @Column
    private String cancelReason; // lý do hủy đơn hàng
    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
