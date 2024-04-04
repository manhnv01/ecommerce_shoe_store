package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 50, message = NAME_MAX_LENGTH_50)
    private String name;
    @Length(min = 10, max = 10, message = PHONE_NUMBER_MUST_HAVE_10_DIGITS)
    private String phone;
    @NotBlank(message = ADDRESS_NOT_BLANK)
    @Length(max = 100, message = ADDRESS_MAX_LENGTH_100)
    private String address;
}
