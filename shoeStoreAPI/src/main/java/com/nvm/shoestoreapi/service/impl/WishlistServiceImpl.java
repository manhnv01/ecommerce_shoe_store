package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.repository.ProductRepository;
import com.nvm.shoestoreapi.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishlistServiceImpl {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private ProductRepository productRepository;

}
