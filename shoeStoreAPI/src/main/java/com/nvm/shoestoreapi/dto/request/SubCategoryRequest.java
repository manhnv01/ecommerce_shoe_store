package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryRequest {
    private Long id;
    @NotBlank(message = "Vui lòng nhập tên danh mục !")
    private String name;
    private Long categoryId;
}
