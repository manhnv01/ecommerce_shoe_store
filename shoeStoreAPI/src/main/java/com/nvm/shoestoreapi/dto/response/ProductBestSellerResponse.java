package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBestSellerResponse {
    private Long id;
    private String name;
    private String slug;
    private String thumbnail;
    private Long quantitySold;
}
