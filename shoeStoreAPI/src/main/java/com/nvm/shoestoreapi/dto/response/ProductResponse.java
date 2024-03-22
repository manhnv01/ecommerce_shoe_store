package com.nvm.shoestoreapi.dto.response;

import com.nvm.shoestoreapi.dto.request.ProductColorRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String thumbnail;
    private List<String> images;
    private Long price;
    private Long totalQuantity;
    private boolean enabled;
    private Long salePrice;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private Long saleId;
    private String saleName;
    private Date startDate;
    private Date endDate;
    private int discount;
    private Date createdAt;
    private Date updatedAt;
    private Long quantitySold;
    private Long countColor;
    private List<ProductColorResponse> productColors;
}
