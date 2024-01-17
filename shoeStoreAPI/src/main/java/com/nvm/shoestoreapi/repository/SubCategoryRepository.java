package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    boolean existsByName(String name);
}
