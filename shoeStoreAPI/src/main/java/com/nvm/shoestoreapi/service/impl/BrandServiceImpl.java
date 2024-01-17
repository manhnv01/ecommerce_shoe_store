package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.repository.BrandRepository;
import com.nvm.shoestoreapi.service.BrandService;
import com.nvm.shoestoreapi.service.StorageService;
import com.nvm.shoestoreapi.util.SlugUtil;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private StorageService storageService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @SneakyThrows
    @Override
    public Brand createBrand(BrandRequest brandRequest) {
        if (brandRepository.existsByName(brandRequest.getName()))
            throw new RuntimeException("Tên nhãn hàng đã tồn tại !");
        Brand brand = new Brand();
        MultipartFile multipartFile = brandRequest.getFile();
        String fileName = storageService.saveFile(multipartFile);
        brandRequest.setThumbnail(fileName);
        modelMapper.map(brandRequest, brand);
        brand.setSlug(slugUtil.toSlug(brandRequest.getName()));
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(BrandRequest brandRequest) {
        Optional<Brand> existingBrand = brandRepository.findById(brandRequest.getId());
        if (existingBrand.isPresent()) {
            Brand brand = existingBrand.get();
            if (!brand.getName().equals(brandRequest.getName()) && brandRepository.existsByName(brandRequest.getName()))
                throw new RuntimeException("Tên nhãn hàng đã tồn tại !");
            brand.setName(brandRequest.getName());
            brand.setSlug(slugUtil.toSlug(brandRequest.getName()));
            MultipartFile multipartFile = brandRequest.getFile();
            String fileName = storageService.saveFile(multipartFile);
            try {
                storageService.deleteFile(brand.getThumbnail());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            brand.setThumbnail(fileName);
            return brandRepository.save(brand);
        }
        else
            throw new RuntimeException("nhãn hàng không tồn tại !");
    }

    @Override
    public void deleteBrandById(Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.isPresent()) {
            brandRepository.deleteById(id);
            try {
                storageService.deleteFile(brand.get().getThumbnail());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else
            throw new RuntimeException("Nhãn hàng không tồn tại !");
    }
}
