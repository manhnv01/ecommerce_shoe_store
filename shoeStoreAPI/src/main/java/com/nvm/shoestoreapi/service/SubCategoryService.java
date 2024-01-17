package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.request.SubCategoryRequest;
import com.nvm.shoestoreapi.entity.SubCategory;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> findAll();
    SubCategory createSubCategory (SubCategoryRequest subCategoryRequest);
    SubCategory updateSubCategory (Long id, SubCategoryRequest subCategoryRequest);
    void deleteSubCategoryById(Long id);
}
