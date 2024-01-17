package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.AddressRequest;
import com.nvm.shoestoreapi.entity.Address;
import com.nvm.shoestoreapi.entity.Category;
import com.nvm.shoestoreapi.repository.AddressRepository;
import com.nvm.shoestoreapi.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address createAddress(AddressRequest addressRequest) {
        Address address = new Address();
        modelMapper.map(addressRequest, address);
        //chưa xử ly nếu là lần đầu thêm địa chỉ cho user thì set Default = true
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(AddressRequest addressRequest) {
        Optional<Address> optionalAddress = addressRepository.findById(addressRequest.getId());
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            modelMapper.map(addressRequest, address);
            return addressRepository.save(address);
        }
        else
            throw new RuntimeException("Địa chỉ không tồn tại !");
    }

    @Override
    public void deleteAddressById(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            addressRepository.deleteById(id);
        }
        else
            throw new RuntimeException("Địa chỉ không tồn tại !");
    }
}
