package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = NAME_NOT_BLANK)
    @Length(max = 30, message = NAME_MAX_LENGTH_30)
    private String name;
    @Email(message = EMAIL_NOT_VALID)
    private String email;
    @Length(min = 6, max = 30, message = PASSWORD_LENGTH_6_30)
    private String password;
}
