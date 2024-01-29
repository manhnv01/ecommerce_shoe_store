package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.ProductService;
import com.nvm.shoestoreapi.service.StorageService;
import com.nvm.shoestoreapi.util.SlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StorageService storageService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductColorRepository productColorRepository;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);
        product.setSlug(slugUtil.toSlug(productRequest.getName()));

        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + productRequest.getBrandId()));

        Category category = categoryRepository.findById(productRequest.getSubCategoryId())
                .orElseThrow(() -> new RuntimeException("SubCategory not found with id: " + productRequest.getSubCategoryId()));

        product.setBrand(brand);
        product.setCategory(category);

        List<String> tags = productRequest.getTags().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        product.setTags(tags);

        MultipartFile thumbnailP = productRequest.getFile();
        product.setThumbnail(storageService.saveFile(thumbnailP));

        Product savedProduct = productRepository.save(product);

        List<ProductColor> productColors = productRequest.getProductColors().stream()
                .map(productColorRequest -> mapToProductColor(productColorRequest, savedProduct))
                .collect(Collectors.toList());

        //productColorRepository.saveAll(productColors);
        for (ProductColor productColor : productColors) {
            productColorRepository.save(productColor);
            List<ProductDetails> productDetailsList = productColor.getProductDetails().stream()
                    .map(productDetailsRequest -> mapToProductDetails(productDetailsRequest, productColor))
                    .collect(Collectors.toList());
            productColor.setProductDetails(productDetailsList);
            productDetailsRepository.saveAll(productDetailsList);
        }

        return savedProduct;
    }

    private ProductColor mapToProductColor(ProductColorRequest productColorRequest, Product product) {
        ProductColor productColor = modelMapper.map(productColorRequest, ProductColor.class);
        productColor.setProduct(product);
        productColor.setThumbnail(storageService.saveFile(productColorRequest.getFile()));

        List<String> fileNamesImages = storageService.saveFiles(productColorRequest.getFiles());
        productColor.setImages(fileNamesImages);

        return productColor;
    }

    private ProductDetails mapToProductDetails(ProductDetails productDetailsRequest, ProductColor productColor) {
        ProductDetails productDetails = modelMapper.map(productDetailsRequest, ProductDetails.class);
        productDetails.setQuantity(0);
        productDetails.setProductColor(productColor);
        return productDetails;
    }

    @Override
    public Product updateProduct(Long id, ProductRequest productRequest) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            existingProduct.get().setName(productRequest.getName());
            existingProduct.get().setSlug(slugUtil.toSlug(productRequest.getName()));
            modelMapper.map(productRequest, existingProduct);
            return productRepository.save(product);
        } else
            throw new RuntimeException("Nhãn hàng không tồn tại !");
    }

    @Override
    public void deleteProductById(Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            productRepository.deleteById(id);
        } else
            throw new RuntimeException("Nhãn hàng không tồn tại !");
    }
}
