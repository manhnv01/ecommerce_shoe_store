package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.CategoryRequest;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.repository.CategoryRepository;
import com.nvm.shoestoreapi.service.CategoryService;
import com.nvm.shoestoreapi.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName()))
            throw new RuntimeException(DUPLICATE_NAME);
        if (categoryRequest.getSlug() == null || categoryRequest.getSlug().isEmpty())
            categoryRequest.setSlug(slugUtil.toSlug(categoryRequest.getName()));
        if (categoryRepository.existsBySlug(categoryRequest.getSlug()))
            throw new RuntimeException(DUPLICATE_SLUG);
        Category category = new Category();
        category.setName(categoryRequest.getName().trim());
        category.setSlug(categoryRequest.getSlug());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(CategoryRequest categoryRequest) {
        Optional<Category> existingCategory = categoryRepository.findById(categoryRequest.getId());
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            if (!category.getName().equals(categoryRequest.getName()) && categoryRepository.existsByName(categoryRequest.getName()))
                throw new RuntimeException(DUPLICATE_NAME);
            if (categoryRequest.getSlug() == null || categoryRequest.getSlug().isEmpty())
                categoryRequest.setSlug(slugUtil.toSlug(categoryRequest.getName()));
            if (!category.getSlug().equals(categoryRequest.getSlug()) && categoryRepository.existsBySlug(categoryRequest.getSlug()))
                throw new RuntimeException(DUPLICATE_SLUG);
            category.setName(categoryRequest.getName().trim());
            category.setSlug(categoryRequest.getSlug());
            return categoryRepository.save(category);
        } else
            throw new RuntimeException(CATEGORY_NOT_FOUND);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            if (categoryRepository.existsByIdAndProductsIsNotEmpty(id))
                throw new RuntimeException(CANNOT_DELETE_CATEGORY);
            categoryRepository.deleteById(id);
        } else
            throw new RuntimeException(CATEGORY_NOT_FOUND);
    }

    @Override
    public void updatesStatus(List<Long> categoryIds, boolean enabled) {
        for (Long id : categoryIds) {
            Optional<Category> existingCategory = categoryRepository.findById(id);
            if (existingCategory.isPresent()) {
                existingCategory.get().setEnabled(enabled);
                categoryRepository.save(existingCategory.get());
            } else {
                throw new RuntimeException(CATEGORY_NOT_FOUND);
            }
        }
        categoryRepository.saveAll(categoryRepository.findAllById(categoryIds));
    }

    @Override
    public Page<Category> findByNameContaining(String name, Pageable pageable) {
        return categoryRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Category> findByEnabled(boolean enabled, Pageable pageable) {
        return categoryRepository.findByEnabled(enabled, pageable);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public long countByEnabledTrue() {
        return categoryRepository.countByEnabledTrue();
    }

    @Override
    public long countByEnabledFalse() {
        return categoryRepository.countByEnabledFalse();
    }

    @Override
    public Optional<Category> findById(Long id) {
        if (!categoryRepository.existsById(id))
            throw new RuntimeException(CATEGORY_NOT_FOUND);
        return categoryRepository.findById(id);
    }

}
