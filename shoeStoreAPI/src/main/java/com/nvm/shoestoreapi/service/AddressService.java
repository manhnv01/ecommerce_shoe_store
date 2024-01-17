package com.nvm.shoestoreapi.service;

import com.nvm.shoestoreapi.dto.request.AddressRequest;
import com.nvm.shoestoreapi.dto.request.CartRequest;
import com.nvm.shoestoreapi.entity.Address;
import com.nvm.shoestoreapi.entity.Cart;

import java.util.List;

public interface AddressService {
    List<Address> findAll();
    Address createAddress(AddressRequest addressRequest);
    Address updateAddress(AddressRequest addressRequest);
    void deleteAddressById(Long id);
}
