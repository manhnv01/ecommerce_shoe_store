package com.nvm.shoestoreapi.dto.goship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParcelRequest {
    private String cod;
    private String weight;
    private String width;
    private String height;
    private String length;
}
