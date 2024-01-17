package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.dto.request.SubCategoryRequest;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.entity.SubCategory;
import com.nvm.shoestoreapi.repository.CategoryRepository;
import com.nvm.shoestoreapi.repository.SubCategoryRepository;
import com.nvm.shoestoreapi.service.SubCategoryService;
import com.nvm.shoestoreapi.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();

    @Override
    public List<SubCategory> findAll() {
        return subCategoryRepository.findAll();
    }

    @Override
    public SubCategory createSubCategory(SubCategoryRequest subCategoryRequest) {
        if (subCategoryRepository.existsByName(subCategoryRequest.getName()))
            throw new RuntimeException("Tên danh mục đã tồn tại !");
        SubCategory subCategory = new SubCategory();
        modelMapper.map(subCategoryRequest, subCategory);
        subCategory.setSlug(slugUtil.toSlug(subCategoryRequest.getName()));
        Optional<Category> categoryOptional = categoryRepository.findById(subCategoryRequest.getCategoryId());

        if (categoryOptional.isPresent()){
            subCategory.setCategory(categoryOptional.get());
            return subCategoryRepository.save(subCategory);
        }
        else
            throw new RuntimeException("Không tìm thấy danh mục này !");
    }

    @Override
    public SubCategory updateSubCategory(Long id, SubCategoryRequest subCategoryRequest) {
        Optional<SubCategory> existingCategory = subCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            SubCategory category = existingCategory.get();
            if (subCategoryRepository.existsByName(subCategoryRequest.getName()))
                throw new RuntimeException("Tên danh mục đã tồn tại !");
            existingCategory.get().setName(subCategoryRequest.getName());
            existingCategory.get().setSlug(slugUtil.toSlug(subCategoryRequest.getName()));
            modelMapper.map(subCategoryRequest, existingCategory);
            return subCategoryRepository.save(category);
        }
        else
            throw new RuntimeException("Danh mục không tồn tại !");
    }

    @Override
    public void deleteSubCategoryById(Long id) {
        Optional<SubCategory> existingCategory = subCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            subCategoryRepository.deleteById(id);
        }
        else
            throw new RuntimeException("Danh mục không tồn tại !");
    }

}
