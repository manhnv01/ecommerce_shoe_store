package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private Long id;
    @NotBlank(message = "Vui lòng nhập tên danh mục!")
    @Length(min = 2, max = 30, message = "Tên danh mục phải từ 2 đến 30 ký tự!")
    private String name;
    @NotBlank(message = "Vui lòng nhập slug!")
    @Length(min = 2, max = 50, message = "Slug phải từ 2 đến 50 ký tự!")
    private String slug;
    private boolean enabled;
}
