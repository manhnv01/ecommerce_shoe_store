package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.repository.BrandRepository;
import com.nvm.shoestoreapi.service.BrandService;
import com.nvm.shoestoreapi.service.StorageService;
import com.nvm.shoestoreapi.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private StorageService storageService;
    private final SlugUtil slugUtil = new SlugUtil();

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    @Override
    public Brand create(BrandRequest brandRequest) {
        if (brandRepository.existsByName(brandRequest.getName()))
            throw new RuntimeException(DUPLICATE_NAME);
        if (brandRequest.getSlug() == null || brandRequest.getSlug().isEmpty())
            brandRequest.setSlug(slugUtil.toSlug(brandRequest.getName()));
        if (brandRepository.existsBySlug(brandRequest.getSlug()))
            throw new RuntimeException(DUPLICATE_SLUG);
        Brand brand = new Brand();
        brand.setName(brandRequest.getName().trim());
        brand.setSlug(brandRequest.getSlug());
        brand.setEnabled(brandRequest.isEnabled());
        brand.setThumbnail(storageService.saveFile(brandRequest.getFile()));
        return brandRepository.save(brand);
    }

    @Override
    public Brand update(BrandRequest brandRequest) {
        Optional<Brand> exist = brandRepository.findById(brandRequest.getId());
        if (exist.isPresent()) {
            Brand brand = exist.get();
            if (!brand.getName().equals(brandRequest.getName()) && brandRepository.existsByName(brandRequest.getName()))
                throw new RuntimeException(DUPLICATE_NAME);
            if (brandRequest.getSlug() == null || brandRequest.getSlug().isEmpty())
                brand.setSlug(slugUtil.toSlug(brandRequest.getName()));
            if (!brand.getSlug().equals(brandRequest.getSlug()) && brandRepository.existsBySlug(brandRequest.getSlug()))
                throw new RuntimeException(DUPLICATE_SLUG);
            brand.setName(brandRequest.getName().trim());
            brand.setSlug(brandRequest.getSlug());
            brand.setEnabled(brandRequest.isEnabled());
            if (!brandRequest.getFile().isEmpty()) {
                storageService.deleteFile(brand.getThumbnail());
                brand.setThumbnail(storageService.saveFile(brandRequest.getFile()));
            }
            return brandRepository.save(brand);
        } else
            throw new RuntimeException(BRAND_NOT_FOUND);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Brand> exist = brandRepository.findById(id);
        if (exist.isPresent()) {
            if (brandRepository.existsByIdAndProductsIsNotEmpty(id))
                throw new RuntimeException(CANNOT_DELETE_BRAND);
            storageService.deleteFile(exist.get().getThumbnail());
            brandRepository.deleteById(id);
        } else
            throw new RuntimeException(BRAND_NOT_FOUND);
    }

    @Override
    public void updatesStatus(List<Long> ids, boolean enabled) {
        for (Long id : ids) {
            Optional<Brand> exist = brandRepository.findById(id);
            if (exist.isPresent()) {
                exist.get().setEnabled(enabled);
                brandRepository.save(exist.get());
            } else {
                throw new RuntimeException(BRAND_NOT_FOUND);
            }
        }
        brandRepository.saveAll(brandRepository.findAllById(ids));
    }

    @Override
    public Page<Brand> findByNameContaining(String name, Pageable pageable) {
        return brandRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Brand> findByEnabled(boolean enabled, Pageable pageable) {
        return brandRepository.findByEnabled(enabled, pageable);
    }

    @Override
    public Page<Brand> searchByNameAndStatus(String name, boolean enabled, Pageable pageable) {
        return brandRepository.findByNameContainingAndEnabled(name, enabled, pageable);
    }

    @Override
    public void changeSwitch(Long id) {
        Optional<Brand> existing = brandRepository.findById(id);
        if (existing.isPresent()) {
            existing.get().setEnabled(!existing.get().isEnabled());
            brandRepository.save(existing.get());
        } else {
            throw new RuntimeException(BRAND_NOT_FOUND);
        }
    }

    @Override
    public long count() {
        return brandRepository.count();
    }

    @Override
    public long countByEnabledTrue() {
        return brandRepository.countByEnabledTrue();
    }

    @Override
    public long countByEnabledFalse() {
        return brandRepository.countByEnabledFalse();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        if (!brandRepository.existsById(id))
            throw new RuntimeException(BRAND_NOT_FOUND);
        return brandRepository.findById(id);
    }

}
