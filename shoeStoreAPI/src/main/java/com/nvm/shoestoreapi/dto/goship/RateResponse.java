package com.nvm.shoestoreapi.dto.goship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {
    private String id;
    private String carrier_name;
    private String carrier_logo;
    private String carrier_short_name;
    private String service;
    private String expected;
    private String total_fee;
}
