package com.nvm.shoestoreapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private Long id;
    private String name;
    private Long userId;
    private Long phone;
    private String city;
    private String district;
    private String ward;
    private String detail;
    private boolean isDefault;
}
