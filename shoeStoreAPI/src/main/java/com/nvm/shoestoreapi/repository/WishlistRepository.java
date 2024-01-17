package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}