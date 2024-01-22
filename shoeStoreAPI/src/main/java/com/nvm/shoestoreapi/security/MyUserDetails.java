package com.nvm.shoestoreapi.security;

import com.nvm.shoestoreapi.entity.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class MyUserDetails implements UserDetails {

    private Account account;

    public MyUserDetails(Account account) {
        this.account = account;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        account.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }


    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        //return account.getName();
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.getVerificationCodeExpirationDate() == null || account.getVerificationCodeExpirationDate().getTime() > System.currentTimeMillis();
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }

    public Long getUserId() {
        return account.getId();
    }

    public String getEmail() {
        return account.getEmail();
    }
}
