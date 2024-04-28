package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "return_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private boolean status; // Trạng thái trả hàng (0: Chưa xử lý, 1: Đã xử lý)
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @OneToMany(mappedBy = "returnProduct")
    @JsonIgnore
    private List<ReturnProductDetails> returnProductDetails;
}
