package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "receipts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receipt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @OneToMany(mappedBy = "receipt")
    private List<ReceiptDetails> receiptDetails;
}
