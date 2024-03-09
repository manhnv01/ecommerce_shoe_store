package com.nvm.shoestoreapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponse implements Serializable {
    private Long id;
    private String size;
    private Integer quantity;
}
