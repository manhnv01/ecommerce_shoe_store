package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "receiptDetails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int quantity;
    @Column
    private Long price;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private ProductDetails productDetails;
    @ManyToOne
    @JoinColumn(name = "receipt_id")
    @JsonIgnore
    private Receipt receipt;
}
