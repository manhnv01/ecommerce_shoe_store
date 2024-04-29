package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import java.util.Date;

import static com.nvm.shoestoreapi.util.Constant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String city;
    private String district;
    private String ward;
    private String addressDetail;

    private Long totalOrder;
    private Long totalSpent;
    private Date LastOrderDate;
    private String LastOrderText;

    private Date createdAt;
}
