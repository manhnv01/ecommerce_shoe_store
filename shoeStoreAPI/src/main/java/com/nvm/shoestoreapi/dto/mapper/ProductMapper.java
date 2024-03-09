package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.request.ReceiptDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.response.*;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.entity.ReceiptDetails;
import com.nvm.shoestoreapi.entity.Sale;
import com.nvm.shoestoreapi.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductMapper {
    final ModelMapper modelMapper;
    final SaleRepository saleRepository;
    final ProductDetailsRepository productDetailsRepository;

    public ProductResponse convertToResponse(Product product) {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setBrandName(product.getBrand().getName());
        productResponse.setCategoryName(product.getCategory().getName());
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());

        List<Sale> activeSales = product.getSales().stream()
                .filter(sale -> sale.getStartDate().before(new Date()) && sale.getEndDate().after(new Date()))
                .collect(Collectors.toList());

        if (!activeSales.isEmpty()) {
            Sale activeSale = activeSales.get(0);
            productResponse.setSaleId(activeSale.getId());
            productResponse.setSaleName(activeSale.getName());
            productResponse.setStartDate(activeSale.getStartDate());
            productResponse.setEndDate(activeSale.getEndDate());
            productResponse.setDiscount(activeSale.getDiscount());

            // Set sale price only if the sale is currently active
            productResponse.setSalePrice(product.getPrice() - (product.getPrice() * activeSale.getDiscount() / 100));
        }

        // TODO: Add product colors and product details to the productResponse
        productResponse.setProductColors(product.getProductColors().stream().map(productColor -> {
            ProductColorResponse productColorResponse = modelMapper.map(productColor, ProductColorResponse.class);
            productColorResponse.setProductDetails(productColor.getProductDetails().stream().map(productDetails -> {
                ProductDetailsResponse productDetailsResponse = modelMapper.map(productDetails, ProductDetailsResponse.class);
                productDetailsResponse.setSize(productDetails.getSize());
                productDetailsResponse.setQuantity(productDetails.getQuantity());
                return productDetailsResponse;
            }).collect(Collectors.toList()));
            return productColorResponse;
        }).collect(Collectors.toList()));
        return productResponse;
    }
}
