package com.nvm.shoestoreapi.dto.response;

import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private Long price;
    private int discount;
    private String status;
    private Long subCategoryId;
    private Long brandId;
    private MultipartFile file;
    private List<String> tags = new ArrayList<>();
    private List<ProductColorRequest> productColors = new ArrayList<>();
}
