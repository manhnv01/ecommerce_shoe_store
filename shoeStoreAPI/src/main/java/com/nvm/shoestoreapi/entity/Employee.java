package com.nvm.shoestoreapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String gender;
    @Column
    private String birthday;
    @Column
    private String avatar;
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;
    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    private List<Receipt> receipts;
}
