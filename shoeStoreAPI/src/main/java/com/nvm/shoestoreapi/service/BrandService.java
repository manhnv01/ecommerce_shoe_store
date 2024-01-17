package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Category;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand createBrand (BrandRequest brandRequest);
    Brand updateBrand (BrandRequest brandRequest);
    void deleteBrandById(Long id);
}
