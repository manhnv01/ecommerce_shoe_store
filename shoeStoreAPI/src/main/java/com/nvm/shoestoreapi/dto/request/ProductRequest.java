package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    @Min(value = 0 , message = "Giá tiền phải lớn hơn hoặc bằng 0 !")
    @Max(value = 1000000000 , message = "Giá tiền quá lớn kiểm tra lại !")
    private Long price;
    @Min(value = 0 , message = "Giá giảm phải lớn hơn hoặc bằng 0")
    @Max(value = 100 , message = "Giá giảm phải nhỏ hơn hoặc bằng 100")
    private int discount;
    private String status;
    private Long subCategoryId;
    private Long brandId;
    private MultipartFile file;
    private List<String> tags = new ArrayList<>();
    @Valid
    private List<ProductColorRequest> productColors = new ArrayList<>();
}
