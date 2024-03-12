package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.Date;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 50, message = NAME_MAX_LENGTH_50)
    private String name;
    @Length(min = 10, max = 10, message = PHONE_NUMBER_MUST_HAVE_10_DIGITS)
    private String phone;
    private String gender;
    @NotBlank(message = BIRTHDAY_NOT_BLANK)
    private Date birthday;
    private String avatar;
    @NotBlank(message = STATUS_NOT_BLANK)
    private String status;
    @Email(message = EMAIL_NOT_VALID)
    private String email;
    private MultipartFile file;
}
