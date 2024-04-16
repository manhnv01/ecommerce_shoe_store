package com.nvm.shoestoreapi.dto.goship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {
    private AddressRequest address_from;
    private AddressRequest address_to;
    private ParcelRequest parcel;
}
