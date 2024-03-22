package com.nvm.shoestoreapi.dto.mapper;

import com.nvm.shoestoreapi.dto.request.ReceiptDetailsRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.dto.response.*;
import com.nvm.shoestoreapi.entity.*;
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
    final OrderRepository orderRepository;
    final OrderDetailsRepository orderDetailsRepository;

    public ProductResponse convertToResponse(Product product) {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        productResponse.setBrandName(product.getBrand().getName());
        productResponse.setCategoryName(product.getCategory().getName());
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setEnabled(product.isEnabled());

        productResponse.setTotalQuantity(product.getProductColors().stream()
                .flatMap(productColor -> productColor.getProductDetails().stream())
                .mapToLong(ProductDetails::getQuantity)
                .sum());

        // tinh so luong da ban
        productResponse.setQuantitySold(orderDetailsRepository.countByProductDetails_ProductColor_Product_Id(product.getId()));

        productResponse.setCountColor((long) product.getProductColors().size());
        
        if (product.getSales() != null) {
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
        }

        // TODO: Add product colors and product details to the productResponse
        productResponse.setProductColors(product.getProductColors().stream().map(productColor -> {
            ProductColorResponse productColorResponse = modelMapper.map(productColor, ProductColorResponse.class);
            productColorResponse.setColorQuantity(productColor.getProductDetails().stream().mapToLong(ProductDetails::getQuantity).sum());
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
