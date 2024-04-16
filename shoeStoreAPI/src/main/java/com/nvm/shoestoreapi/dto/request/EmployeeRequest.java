package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 30, message = NAME_MAX_LENGTH_30)
    private String name;
    @Length(min = 10, max = 10, message = PHONE_NUMBER_MUST_HAVE_10_DIGITS)
    private String phone;
    @NotBlank(message = GENDER_NOT_BLANK)
    private String gender;
    @NotNull(message = BIRTHDAY_NOT_BLANK)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date birthday;
    private String avatar;
    @NotBlank(message = STATUS_NOT_BLANK)
    private String status;
    @Email(message = EMAIL_NOT_VALID)
    private String email;
    @NotBlank(message = CITY_NOT_BLANK)
    private String city;
    @NotBlank(message = DISTRICT_NOT_BLANK)
    private String district;
    @NotBlank(message = WARD_NOT_BLANK)
    private String ward;
    @NotBlank(message = ADDRESS_DETAIL_NOT_BLANK)
    private String addressDetail;
    private MultipartFile file;
}
