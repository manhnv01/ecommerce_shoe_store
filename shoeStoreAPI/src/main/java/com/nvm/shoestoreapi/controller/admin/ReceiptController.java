package com.nvm.shoestoreapi.controller.admin;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.ReceiptRequest;
import com.nvm.shoestoreapi.entity.Brand;
import com.nvm.shoestoreapi.entity.Receipt;
import com.nvm.shoestoreapi.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping(value = "")
    public ResponseEntity<?> createReceipt(
            @Valid @RequestBody ReceiptRequest receiptRequest,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError());
        }

        return ResponseEntity.ok(receiptService.createReceipt(receiptRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<Receipt>> getAllReceipts() {
        List<Receipt> receipts = receiptService.findAll();
        return ResponseEntity.ok(receipts);
    }
}
