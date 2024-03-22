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
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String gender;
    @Column
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column
    private String avatar;
    @Column
    private String city;
    @Column
    private String district;
    @Column
    private String ward;
    @Column
    private String addressDetail;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
