package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

import static com.nvm.shoestoreapi.util.Constant.NAME_MAX_LENGTH_30;
import static com.nvm.shoestoreapi.util.Constant.NAME_NOT_BLANK;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 30, message = NAME_MAX_LENGTH_30)
    private String name;
    private String slug;
    private boolean enabled;
    private String thumbnail;
    private MultipartFile file;
}
