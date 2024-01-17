package com.nvm.shoestoreapi.repository;

import com.nvm.shoestoreapi.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}