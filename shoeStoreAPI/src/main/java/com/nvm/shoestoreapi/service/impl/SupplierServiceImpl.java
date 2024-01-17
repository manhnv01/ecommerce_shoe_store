package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.BrandRequest;
import com.nvm.shoestoreapi.dto.request.SupplierRequest;
import com.nvm.shoestoreapi.entity.Supplier;
import com.nvm.shoestoreapi.repository.SupplierRepository;
import com.nvm.shoestoreapi.service.SupplierService;
import com.nvm.shoestoreapi.util.SlugUtil;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final SlugUtil slugUtil = new SlugUtil();


    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @SneakyThrows
    @Override
    public Supplier createSupplier(SupplierRequest supplierRequest) {
        if (supplierRepository.existsByName(supplierRequest.getName()))
            throw new RuntimeException("Tên nhãn hàng đã tồn tại !");
        Supplier supplier = new Supplier();
        modelMapper.map(supplierRequest, supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, SupplierRequest supplierRequest) {
        Optional<Supplier> existingSupplier = supplierRepository.findById(id);
        if (existingSupplier.isPresent()) {
            Supplier Brand = existingSupplier.get();
            if (supplierRepository.existsByName(supplierRequest.getName()))
                throw new RuntimeException("Tên nhãn hàng đã tồn tại !");
            existingSupplier.get().setName(supplierRequest.getName());
            modelMapper.map(supplierRequest, existingSupplier);
            return supplierRepository.save(Brand);
        }
        else
            throw new RuntimeException("nhãn hàng không tồn tại !");
    }

    @Override
    public void deleteSupplierById(Long id) {
        Optional<Supplier> existingSupplier = supplierRepository.findById(id);
        if (existingSupplier.isPresent()) {
            supplierRepository.deleteById(id);
        }
        else
            throw new RuntimeException("nhãn hàng không tồn tại !");
    }
}
