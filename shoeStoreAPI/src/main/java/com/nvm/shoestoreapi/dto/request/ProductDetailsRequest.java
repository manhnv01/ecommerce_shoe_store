package com.nvm.shoestoreapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvm.shoestoreapi.entity.ProductColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsRequest {
    private String size;
    private Long productColorId;
}
