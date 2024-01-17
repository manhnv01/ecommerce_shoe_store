package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
