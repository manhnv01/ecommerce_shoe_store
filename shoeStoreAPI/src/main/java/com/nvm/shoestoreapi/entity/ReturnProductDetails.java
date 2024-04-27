package com.nvm.shoestoreapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "return_product_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int quantity;
    @Column
    private String reason;
    @ManyToOne
    @JoinColumn(name = "product_details_id")
    private ProductDetails productDetails;
    @ManyToOne
    @JoinColumn(name = "return_product_id")
    private ReturnProduct returnProduct;
}
