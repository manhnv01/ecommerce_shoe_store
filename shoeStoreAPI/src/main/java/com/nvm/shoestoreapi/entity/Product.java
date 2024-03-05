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
public class Product extends BaseEntity{
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
    private boolean enabled;
    @ElementCollection
    @CollectionTable(name = "product_image", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private List<String> images;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @OneToMany(mappedBy = "product")
    private List<ProductColor> productColors;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Wishlist> wishlists;
}
