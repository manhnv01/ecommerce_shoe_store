package com.nvm.shoestoreapi.mapper;

import com.nvm.shoestoreapi.dto.request.ProductRequest;
import com.nvm.shoestoreapi.dto.response.ProductResponse;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.repository.BrandRepository;
import com.nvm.shoestoreapi.repository.ProductRepository;
import com.nvm.shoestoreapi.repository.SubCategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper {
    final ModelMapper modelMapper;
    final SubCategoryRepository subCategoryRepository;
    final BrandRepository brandRepository;
    final ProductRepository productRepository;


//    // ProductMapper
//    public ProductResponse convertToResponse(Product product) {
//        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
//        productResponse.setCategoryId(product.getCategory().getId());
//        productResponse.setCategoryName(product.getCategory().getName());
//        productResponse.setMaterialId(product.getMaterial().getId());
//        productResponse.setMaterialName(product.getMaterial().getName());
//        product.getDemands().forEach(demand -> productResponse.getDemands().add(demandMapper.convertToResponse(demand)));
//        product.getProductColors().forEach(productColor -> productResponse.getProductColors().add(convertToResponse(productColor)));
//        return productResponse;
//    }

//    public Product convertToEntity(ProductRequest productRequest) {
//        Product product = modelMapper.map(productRequest, Product.class);
//        product.setSubCategory(subCategoryRepository.findById(productRequest.getSubCategoryId()).orElse(null));
//        product.setBrand(brandRepository.findById(productRequest.getBrandId()).orElse(null));
//        productRequest.getDemands().forEach(demandRequest
//                -> product.getDemands().add(demandMapper.convertToEntity(demandRequest)));
//        productRequest.getProductColors().forEach(productColorRequest
//                -> product.getProductColors().add(convertToEntity(productColorRequest)));
//        return product;
//    }
//
//    // ProductColorMapper
//    public ProductColorResponse convertToResponse(ProductColor productColor) {
//        ProductColorResponse productColorResponse = modelMapper.map(productColor, ProductColorResponse.class);
//        productColor.getProductColorSizes().forEach(
//                productColorSize -> productColorResponse.getProductColorSizes().add(convertToResponse(productColorSize))
//        );
//        return modelMapper.map(productColor, ProductColorResponse.class);
//    }
//
//    public ProductColor convertToEntity(ProductColorRequest productColorRequest) {
//        ProductColor productColor = modelMapper.map(productColorRequest, ProductColor.class);
//        productColor.setProduct(productRepository.findById(productColorRequest.getProductId()).orElse(null));
//        productColorRequest.getProductColorSizes().forEach(
//                productColorSizeRequest -> productColor.getProductColorSizes().add(convertToEntity(productColorSizeRequest))
//        );
//        return productColor;
//    }

//    // ProductColorSizeMapper
//    public ProductColorSizeResponse convertToResponse(ProductColorSize productColorSize) {
//        return modelMapper.map(productColorSize, ProductColorSizeResponse.class);
//    }
//
//    public ProductColorSize convertToEntity(ProductColorSizeRequest productColorSizeRequest) {
//        return modelMapper.map(productColorSizeRequest, ProductColorSize.class);
//    }
}
