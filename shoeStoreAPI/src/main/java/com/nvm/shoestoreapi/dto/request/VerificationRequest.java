package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

import static com.nvm.shoestoreapi.util.Constant.CODE_LENGTH_6;
import static com.nvm.shoestoreapi.util.Constant.EMAIL_NOT_VALID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequest {
    @Length(min = 6, max = 6, message = CODE_LENGTH_6)
    private String code;
    @Email(message = EMAIL_NOT_VALID)
    private String email;
}
