package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import static com.nvm.shoestoreapi.util.Constant.PASSWORD_LENGTH_6_30;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private Long id;
    private String oldPassword;
    @Length(min = 6, max = 30, message = PASSWORD_LENGTH_6_30)
    private String newPassword;
}
