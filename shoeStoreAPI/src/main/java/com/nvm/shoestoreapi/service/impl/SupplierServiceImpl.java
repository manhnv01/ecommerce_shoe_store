package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.entity.Supplier;
import com.nvm.shoestoreapi.repository.ReceiptRepository;
import com.nvm.shoestoreapi.repository.SupplierRepository;
import com.nvm.shoestoreapi.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ReceiptRepository receiptRepository;

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Page<Supplier> findAll(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    @Override
    public Supplier create(SupplierRequest supplierRequest) {
        if (supplierRepository.existsByName(supplierRequest.getName()))
            throw new RuntimeException(DUPLICATE_NAME);
        if (supplierRepository.existsByPhone(supplierRequest.getPhone()))
            throw new RuntimeException(DUPLICATE_PHONE);
        if (supplierRepository.existsByEmail(supplierRequest.getEmail()))
            throw new RuntimeException(DUPLICATE_EMAIL);
        Supplier supplier = new Supplier();
        mapRequestToSupplier(supplier, supplierRequest);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(SupplierRequest supplierRequest) {
        Optional<Supplier> existingSupplier = supplierRepository.findById(supplierRequest.getId());
        if (existingSupplier.isPresent()) {
            Supplier supplier = existingSupplier.get();
            if (!supplier.getName().equals(supplierRequest.getName()) && supplierRepository.existsByName(supplierRequest.getName()))
                throw new RuntimeException(DUPLICATE_NAME);
            if (!supplier.getPhone().equals(supplierRequest.getPhone()) && supplierRepository.existsByPhone(supplierRequest.getPhone()))
                throw new RuntimeException(DUPLICATE_PHONE);
            if (!supplier.getEmail().equals(supplierRequest.getEmail()) && supplierRepository.existsByEmail(supplierRequest.getEmail()))
                throw new RuntimeException(DUPLICATE_EMAIL);

            mapRequestToSupplier(supplier, supplierRequest);
            return supplierRepository.save(supplier);
        } else
            throw new RuntimeException(SUPPLIER_NOT_FOUND);
    }

    private void mapRequestToSupplier(Supplier supplier, SupplierRequest supplierRequest) {
        supplier.setName(supplierRequest.getName().trim());
        supplier.setPhone(supplierRequest.getPhone());
        supplier.setAddress(supplierRequest.getAddress());
        supplier.setEmail(supplierRequest.getEmail());
    }

    @Override
    public void deleteById(Long id) {
        Optional<Supplier> existingSupplier = supplierRepository.findById(id);
        if (existingSupplier.isPresent()) {
            boolean existingReceipt = receiptRepository.existsBySupplierId(id);
            if (!existingReceipt) {
                supplierRepository.deleteById(id);
            } else {
                throw new RuntimeException(CANNOT_DELETE_SUPPLIER);
            }
        } else
            throw new RuntimeException(SUPPLIER_NOT_FOUND);
    }

    @Override
    public Page<Supplier> findByNameContaining(String name, Pageable pageable) {
        return supplierRepository.findByNameContaining(name, pageable);
    }

    @Override
    public long count() {
        return supplierRepository.count();
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        if (!supplierRepository.existsById(id))
            throw new RuntimeException(SUPPLIER_NOT_FOUND);
        return supplierRepository.findById(id);
    }

}

