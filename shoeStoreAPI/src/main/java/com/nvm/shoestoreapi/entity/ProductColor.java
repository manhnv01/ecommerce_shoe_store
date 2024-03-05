package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productColors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String color;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    @OneToMany(mappedBy = "productColor")
    private List<ProductDetails> productDetails;
}
