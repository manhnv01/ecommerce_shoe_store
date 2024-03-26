package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

import static com.nvm.shoestoreapi.util.Constant.NAME_NOT_BLANK;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private Long id;
    @NotBlank(message = NAME_NOT_BLANK)
    private String name;
    private String phone;
    private String gender;
    private Date birthday;
    private String city;
    private String district;
    private String ward;
    private String addressDetail;
}
