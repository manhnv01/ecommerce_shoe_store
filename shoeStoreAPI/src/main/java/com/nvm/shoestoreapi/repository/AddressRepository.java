package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}