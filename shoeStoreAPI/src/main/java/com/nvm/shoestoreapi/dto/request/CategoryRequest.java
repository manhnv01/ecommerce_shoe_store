package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 30, message = NAME_MAX_LENGTH_30)
    private String name;
    private String slug;
    private boolean enabled;
}
