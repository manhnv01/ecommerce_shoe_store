package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 50, message = NAME_MAX_LENGTH_50)
    private String name;
    private String slug;
    private String description;
    private String thumbnail;
    @Min(value = 0, message = PRICE_MIN_0)
    @Max(value = 1000000000, message = PRICE_MAX_1_BILLION)
    private Long price;
    private boolean enabled;
    private List<String> images;
    private List<MultipartFile> files;
    @NotNull(message = CATEGORY_NOT_FOUND)
    private Long categoryId;
    @NotNull(message = BRAND_NOT_FOUND)
    private Long brandId;
    private MultipartFile file;
    @Valid
    private List<ProductColorRequest> productColors = new ArrayList<>();
}
