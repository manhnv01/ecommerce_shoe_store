package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.mapper.ProductMapper;
import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.*;
import com.nvm.shoestoreapi.repository.*;
import com.nvm.shoestoreapi.service.ProductService;
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
import java.util.stream.Collectors;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
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
    @Autowired
    private ProductMapper productMapper;

    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();

    private ProductColor mapToProductColor(ProductColorRequest productColorRequest, Product product) {
        ProductColor productColor = modelMapper.map(productColorRequest, ProductColor.class);
        productColor.setProduct(product);

        return productColor;
    }

    @Override
    public ProductResponse create(ProductRequest productRequest) {
        if (productRepository.existsByName(productRequest.getName()))
            throw new RuntimeException(DUPLICATE_NAME);
        if (productRequest.getSlug() == null || productRequest.getSlug().isEmpty())
            productRequest.setSlug(slugUtil.toSlug(productRequest.getName()));
        if (productRepository.existsBySlug(productRequest.getSlug()))
            throw new RuntimeException(DUPLICATE_SLUG);
        if (productRequest.getProductColors().stream().map(ProductColorRequest::getColor).collect(Collectors.toSet()).size()
                != productRequest.getProductColors().size())
            throw new RuntimeException(DUPLICATE_PRODUCT_COLOR);
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException(BRAND_NOT_FOUND));

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND));

        Product product = new Product();
        product.setName(productRequest.getName().trim());
        product.setSlug(productRequest.getSlug());
        product.setBrand(brand);
        product.setCategory(category);
        product.setEnabled(productRequest.isEnabled());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setThumbnail(storageService.saveFile(productRequest.getFile()));
        product.setImages(storageService.saveFiles(productRequest.getFiles()));

        Product savedProduct = productRepository.save(product);

        List<ProductColor> productColors = productRequest.getProductColors().stream()
                .map(productColorRequest -> mapToProductColor(productColorRequest, savedProduct))
                .collect(Collectors.toList());

        productColors.forEach(productColor -> {
            productColorRepository.save(productColor);
            setSizeToProductColor(productColor);
        });

        savedProduct.setProductColors(productColors);
        return productMapper.convertToResponse(productRepository.save(savedProduct));
    }

    @Override
    public ProductResponse update(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId())
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));
        if (!product.getName().equals(productRequest.getName()) && productRepository.existsByName(productRequest.getName()))
            throw new RuntimeException(DUPLICATE_NAME);
        if (productRequest.getSlug() == null || productRequest.getSlug().isEmpty())
            product.setSlug(slugUtil.toSlug(productRequest.getName()));
        if (!product.getSlug().equals(productRequest.getSlug()) && productRepository.existsBySlug(productRequest.getSlug()))
            throw new RuntimeException(DUPLICATE_SLUG);
        for (ProductColorRequest productColorRequest : productRequest.getProductColors()) {
            ProductColor productColor = productColorRequest.getId() != null ?
                    productColorRepository.findById(productColorRequest.getId()).orElse(null) :
                    null;
            if (productColor != null && !productColor.getColor().equalsIgnoreCase(productColorRequest.getColor())
                    && product.getProductColors().stream().map(ProductColor::getColor).collect(Collectors.toSet()).contains(productColorRequest.getColor()))
                throw new RuntimeException(DUPLICATE_PRODUCT_COLOR);
            else if (productColor == null && product.getProductColors().stream().map(ProductColor::getColor).collect(Collectors.toSet()).contains(productColorRequest.getColor()))
                throw new RuntimeException(DUPLICATE_PRODUCT_COLOR);
        }
        product.setBrand(brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new RuntimeException(BRAND_NOT_FOUND)));
        product.setCategory(categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException(CATEGORY_NOT_FOUND)));
        product.setName(productRequest.getName().trim());
        product.setEnabled(productRequest.isEnabled());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());

        if (productRequest.getFile() != null && !productRequest.getFile().isEmpty()) {
            storageService.deleteFile(product.getThumbnail());
            product.setThumbnail(storageService.saveFile(productRequest.getFile()));
        }

        if (productRequest.getFiles() != null && !productRequest.getFiles().isEmpty()) {
            productRequest.getFiles().stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .map(storageService::saveFile)
                    .forEach(product.getImages()::add);
        }

        for (ProductColorRequest productColorRequest : productRequest.getProductColors()) {
            ProductColor productColor = productColorRequest.getId() != null ?
                    productColorRepository.findById(productColorRequest.getId()).orElse(null) :
                    null;

            if (productColor == null) {
                ProductColor newProductColor = new ProductColor();
                newProductColor.setColor(productColorRequest.getColor());
                newProductColor.setProduct(product);

                setSizeToProductColor(newProductColor);
                productColorRepository.save(newProductColor);
            } else {
                productColor.setColor(productColorRequest.getColor());
                productColorRepository.save(productColor);
            }
        }
        return productMapper.convertToResponse(productRepository.save(product));
    }

    @Override
    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::convertToResponse);
    }

    @Override
    public Page<ProductResponse> search(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable)
                .map(productMapper::convertToResponse);
    }

    @Override
    public Page<ProductResponse> searchByStatus(boolean enabled, Pageable pageable) {
        return productRepository.findByEnabled(enabled, pageable)
                .map(productMapper::convertToResponse);
    }

    @Override
    public Optional<ProductResponse> getById(Long id) {
        if (!productRepository.existsById(id))
            throw new RuntimeException(PRODUCT_NOT_FOUND);
        return productRepository.findById(id)
                .map(productMapper::convertToResponse);
    }

    private void setSizeToProductColor(ProductColor productColor) {
        List<ProductDetails> productDetailsList = PRODUCT_SIZE.stream()
                .map(size -> {
                    ProductDetails productDetails = new ProductDetails();
                    productDetails.setSize(size);
                    productDetails.setQuantity(0);
                    productDetails.setProductColor(productColor);
                    return productDetails;
                })
                .collect(Collectors.toList());

        productColor.setProductDetails(productDetailsList);
        productDetailsRepository.saveAll(productDetailsList);
    }

    @Override
    public void updatesStatus(List<Long> ids, boolean enabled) {
        for (Long id : ids) {
            Optional<Product> exist = productRepository.findById(id);
            if (exist.isPresent()) {
                exist.get().setEnabled(enabled);
                productRepository.save(exist.get());
            } else {
                throw new RuntimeException(PRODUCT_NOT_FOUND);
            }
        }
        productRepository.saveAll(productRepository.findAllById(ids));
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public long countByEnabledTrue() {
        return productRepository.countByEnabledTrue();
    }

    @Override
    public long countByEnabledFalse() {
        return productRepository.countByEnabledFalse();
    }

    @Override
    public void deleteProductColor(Long id) {
        Optional<ProductColor> existingProductColor = productColorRepository.findById(id);

        if (existingProductColor.isPresent()) {
            ProductColor productColor = existingProductColor.get();

            for (ProductDetails productDetails : productColor.getProductDetails()) {
                boolean isSizeInPurchaseOrder = !productDetails.getReceiptDetails().isEmpty();
                boolean isSizeInSalesOrder = !productDetails.getOrderDetails().isEmpty();

                if (isSizeInPurchaseOrder || isSizeInSalesOrder) {
                    throw new RuntimeException(CANNOT_DELETE_PRODUCT_DETAILS);
                }
                productDetailsRepository.deleteById(productDetails.getId());
            }
            productColorRepository.deleteById(id);
        } else {
            throw new RuntimeException(PRODUCT_COLOR_NOT_FOUND);
        }
    }

    @Override
    public void changeSwitch(Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            existingProduct.get().setEnabled(!existingProduct.get().isEnabled());
            productRepository.save(existingProduct.get());
        } else {
            throw new RuntimeException(PRODUCT_NOT_FOUND);
        }
    }

    @Override
    public void deleteImageById(Long id, String imageName) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));
        for (int i = 0; i < product.getImages().size(); i++) {
            if (product.getImages().get(i).equals(imageName)) {
                product.getImages().remove(i);
                storageService.deleteFile(imageName);
                productRepository.save(product);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));
        for (ProductColor productColor : product.getProductColors()) {
            deleteProductColor(productColor.getId());
        }
        storageService.deleteFile(product.getThumbnail());
        product.getImages().forEach(storageService::deleteFile);
        productColorRepository.deleteAll(product.getProductColors());
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductColor> findByProductId(Long id) {
        if (!productRepository.existsById(id))
            throw new RuntimeException(PRODUCT_NOT_FOUND);
        return productColorRepository.findByProductId(id);
    }

    @Override
    public List<ProductDetails> findByProductColorId(Long id) {
        if (!productColorRepository.existsById(id))
            throw new RuntimeException(PRODUCT_COLOR_NOT_FOUND);
        return productDetailsRepository.findByProductColorId(id);
    }

    @Override
    public ProductDetails findByProductDetailsId(Long id) {
        return productDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PRODUCT_DETAILS_NOT_FOUND));
    }

    @Override
    public Page<ProductResponse> findAllByEnabledIsTrue(Pageable pageable) {
        return productRepository.findByEnabledIsTrue(pageable)
                .map(productMapper::convertToResponse);
    }

    @Override
    public ProductResponse findBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));
        return productMapper.convertToResponse(product);
    }
}
