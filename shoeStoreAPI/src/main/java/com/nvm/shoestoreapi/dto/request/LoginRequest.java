package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.nvm.shoestoreapi.util.Constant.EMAIL_NOT_VALID;
import static com.nvm.shoestoreapi.util.Constant.PASSWORD_NOT_BLANK;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(message = EMAIL_NOT_VALID)
    private String email;
    @NotBlank(message = PASSWORD_NOT_BLANK)
    private String password;
}
