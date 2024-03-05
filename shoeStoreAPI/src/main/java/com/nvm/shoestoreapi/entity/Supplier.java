package com.nvm.shoestoreapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    private String email;
    @OneToMany(mappedBy = "supplier")
    private List<Receipt> receipts;
}
