package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Account findByVerificationCode(String code);
    boolean existsByEmail(String email);
}