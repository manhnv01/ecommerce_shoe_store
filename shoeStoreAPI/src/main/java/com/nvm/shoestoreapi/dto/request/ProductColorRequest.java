package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.nvm.shoestoreapi.util.Constant.COLOR_MAX_LENGTH_20;
import static com.nvm.shoestoreapi.util.Constant.COLOR_NOT_BLANK;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductColorRequest {
    private Long id;
    @NotBlank(message = COLOR_NOT_BLANK)
    @Length(max = 20, message = COLOR_MAX_LENGTH_20)
    private String color;
}
