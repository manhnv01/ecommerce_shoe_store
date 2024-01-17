package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.entity.Supplier;
import com.nvm.shoestoreapi.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping(value = "")
    public ResponseEntity<?> createSupplier(
            @Valid @ModelAttribute SupplierRequest supplierRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        return ResponseEntity.ok(supplierService.createSupplier(supplierRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateSupplier(
            @Valid @RequestBody SupplierRequest supplierRequest,
            BindingResult result,
            @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable("id") Long id) {
        supplierService.deleteSupplierById(id);
        return ResponseEntity.ok("Xóa nhãn hàng thành công !");
    }

    @GetMapping("")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAll();
        return ResponseEntity.ok(suppliers);
    }
}
