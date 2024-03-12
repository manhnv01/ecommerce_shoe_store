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
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String password;
    @Column(length = 6)
    private String verificationCode;
    @Column
    private boolean isAccountNonLocked;
    @Column
    private boolean isEnabled;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date verificationCodeExpirationDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    @OneToOne(mappedBy = "account")
    @JsonIgnore
    private Customer customer;
    @OneToOne(mappedBy = "account")
    @JsonIgnore
    private Employee employee;
}
