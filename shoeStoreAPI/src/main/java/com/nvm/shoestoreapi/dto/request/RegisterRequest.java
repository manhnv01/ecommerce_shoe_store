package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Vui lòng nhập tên !")
    private String name;
    @NotBlank(message = "Vui lòng nhập email !")
    @Email(message = "Không phải địa chỉ email !")
    private String email;
    @NotBlank(message = "Vui lòng nhập mật khẩu !")
    @Length(min = 6, message = "Mật khẩu phải có từ 6 ký tự tở lên !")
    private String password;
}
