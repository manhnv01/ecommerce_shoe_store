package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    private Long id;
    @NotBlank(message = "Vui lòng nhập tên nhãn hàng !")
    private String name;
    private String thumbnail;
    private MultipartFile file;
}
