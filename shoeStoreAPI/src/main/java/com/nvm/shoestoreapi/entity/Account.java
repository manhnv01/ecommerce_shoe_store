package com.nvm.shoestoreapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private String phone;
    @Column
    private String password;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;
    @Column
    private boolean isAccountNonExpired;
    @Column
    public boolean isAccountNonLocked;
    @Column
    public boolean isCredentialsNonExpired;
    @Column
    public boolean isEnabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    @OneToOne(mappedBy = "account")
    private Customer customer;
    @OneToOne(mappedBy = "account")
    private Employee employee;
}
