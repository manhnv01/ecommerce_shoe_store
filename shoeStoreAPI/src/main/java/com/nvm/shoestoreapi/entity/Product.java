package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String slug;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    @Column(columnDefinition = "LONGTEXT")
    private String thumbnail;
    @Column
    private Long price;
    @Column
    private int discount;
    @Column
    private String status;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonIgnore
    private Brand brand;
    @OneToMany(mappedBy = "product")
    private List<ProductColor> productColors;
    @ElementCollection
    @CollectionTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag_name")
    private List<String> tags;
    @OneToMany(mappedBy = "product")
    private List<Wishlist> wishlists;
}
