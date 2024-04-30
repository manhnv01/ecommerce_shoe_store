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
    private String status; // ( RETURN_PENDING: chờ xử lý;  RETURN_APPROVED: đã xử lý;  RETURN_REJECTED:đã từ chối)
    @Column
    private String reason;
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
