package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorRequest {
    private Long id;
    @NotBlank(message = "Vui lòng nhập màu !")
    private String color;
    private Long productId;
    private String thumbnail;
    private List<ProductDetailsRequest> productDetails;
    private List<String> images;

    private MultipartFile file;
    private List<MultipartFile> files;
}
