package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

import static com.nvm.shoestoreapi.util.Constant.CODE_LENGTH_6;
import static com.nvm.shoestoreapi.util.Constant.PASSWORD_LENGTH_6_30;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @Length(min = 6, max = 6, message = CODE_LENGTH_6)
    private String code;
    @Email
    private String email;
    @Length(min = 6, max = 30, message = PASSWORD_LENGTH_6_30)
    private String newPassword;
}
