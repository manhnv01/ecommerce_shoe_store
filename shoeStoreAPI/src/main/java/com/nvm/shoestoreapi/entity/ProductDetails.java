package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "productDetails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String size;
    @Column
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "productColor_id")
    @JsonIgnore
    private ProductColor productColor;
    @OneToMany(mappedBy = "productDetails")
    @JsonIgnore
    private List<ReceiptDetails> receiptDetails;
    @OneToMany(mappedBy = "productDetails")
    @JsonIgnore
    private List<OrderDetails> orderDetails;
    @OneToMany(mappedBy = "productDetails")
    @JsonIgnore
    private List<CartDetails> cartDetails;
}
