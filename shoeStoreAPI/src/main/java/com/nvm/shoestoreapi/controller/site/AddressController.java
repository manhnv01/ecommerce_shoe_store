package com.nvm.shoestoreapi.controller.site;

import com.nvm.shoestoreapi.dto.request.AddressRequest;
import com.nvm.shoestoreapi.entity.Address;
import com.nvm.shoestoreapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(value = "")
    public ResponseEntity<?> create(
            @Valid @RequestBody AddressRequest addressRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        return ResponseEntity.ok(addressService.createAddress(addressRequest));
    }

    @PutMapping("")
    public ResponseEntity<?> update(
            @Valid @RequestBody AddressRequest addressRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(addressService.updateAddress(addressRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        addressService.deleteAddressById(id);
        return ResponseEntity.ok("Xóa địa chỉ thành công !");
    }

    @GetMapping("")
    public ResponseEntity<List<Address>> getAll() {
        List<Address> addresses = addressService.findAll();
        return ResponseEntity.ok(addresses);
    }
}
