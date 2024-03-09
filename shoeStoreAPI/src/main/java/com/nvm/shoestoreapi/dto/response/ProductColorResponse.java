package com.nvm.shoestoreapi.dto.response;

import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorResponse {
    private Long id;
    private String color;
    private List<ProductDetailsResponse> productDetails;
}
