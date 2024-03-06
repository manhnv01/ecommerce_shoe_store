package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.dto.request.SaleRequest;
import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.Sale;
import com.nvm.shoestoreapi.repository.ProductRepository;
import com.nvm.shoestoreapi.repository.SaleRepository;
import com.nvm.shoestoreapi.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nvm.shoestoreapi.util.Constant.*;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Override
    public Page<Sale> findAll(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    @Override
    public Sale create(SaleRequest saleRequest) {
        if (saleRepository.existsByName(saleRequest.getName()))
            throw new RuntimeException(DUPLICATE_NAME);
        if (saleRequest.getStartDate().after(saleRequest.getEndDate())) {
            throw new RuntimeException(START_DATE_MUST_BE_BEFORE_END_DATE);
        }

        // Kiểm tra ngày bắt đầu và kết thúc có chồng lấp với các sale hiện tại không
        List<Sale> overlappingSales = saleRepository.findByStartDateBetweenOrEndDateBetween(
                saleRequest.getStartDate(), saleRequest.getEndDate(),
                saleRequest.getStartDate(), saleRequest.getEndDate());

        // Kiểm tra trùng lặp productId
        Set<Long> uniqueProductIds = new HashSet<>(saleRequest.getProductIds());
        if (uniqueProductIds.size() < saleRequest.getProductIds().size()) {
            throw new RuntimeException(PRODUCT_ID_NOT_DUPLICATE);
        }

        List<Product> products = productRepository.findAllById(uniqueProductIds);

        // Kiểm tra nếu số lượng products trả về không bằng số lượng productId gửi lên
        if (products.size() != uniqueProductIds.size()) {
            throw new RuntimeException(PRODUCT_NOT_FOUND);
        }

        if (!overlappingSales.isEmpty()) {
            // Kiểm tra xem sản phẩm có trùng với các sale hiện tại không
            for (Sale existingSale : overlappingSales) {
                for (Product existingProduct : existingSale.getProducts()) {
                    if (uniqueProductIds.contains(existingProduct.getId())) {
                        throw new RuntimeException(PRODUCT_ALREADY_IN_SALE);
                    }
                }
            }
        }

        Sale sale = new Sale();
        sale.setName(saleRequest.getName().trim());
        sale.setStartDate(saleRequest.getStartDate());
        sale.setEndDate(saleRequest.getEndDate());
        sale.setDiscount(saleRequest.getDiscount());
        sale.setProducts(products);

        return saleRepository.save(sale);
    }

    @Override
    public Sale update(SaleRequest request) {
        Optional<Sale> existingSale = saleRepository.findById(request.getId());
        if (existingSale.isPresent()) {
            Sale sale = existingSale.get();
            if (!sale.getName().equals(request.getName()) && saleRepository.existsByName(request.getName()))
                throw new RuntimeException(DUPLICATE_NAME);
            if (request.getStartDate().after(request.getEndDate())) {
                throw new RuntimeException(START_DATE_MUST_BE_BEFORE_END_DATE);
            }

            // Kiểm tra ngày bắt đầu và kết thúc có chồng lấp với các sale hiện tại không
            List<Sale> overlappingSales = saleRepository.findByStartDateBetweenOrEndDateBetween(
                    request.getStartDate(), request.getEndDate(),
                    request.getStartDate(), request.getEndDate());

            // Kiểm tra trùng lặp productId
            Set<Long> uniqueProductIds = new HashSet<>(request.getProductIds());
            if (uniqueProductIds.size() < request.getProductIds().size()) {
                throw new RuntimeException(PRODUCT_ID_NOT_DUPLICATE);
            }

            List<Product> products = productRepository.findAllById(uniqueProductIds);

            // Kiểm tra nếu số lượng products trả về không bằng số lượng productId gửi lên
            if (products.size() != uniqueProductIds.size()) {
                throw new RuntimeException(PRODUCT_NOT_FOUND);
            }

            if (!overlappingSales.isEmpty()) {
                // Kiểm tra xem sản phẩm có trùng với các sale hiện tại không
                for (Sale existing : overlappingSales) {
                    for (Product existingProduct : existing.getProducts()) {
                        if (uniqueProductIds.contains(existingProduct.getId())) {
                            throw new RuntimeException(PRODUCT_ALREADY_IN_SALE);
                        }
                    }
                }
            }

            existingSale.get().getProducts().clear();

            sale.setName(request.getName().trim());
            sale.setStartDate(request.getStartDate());
            sale.setEndDate(request.getEndDate());
            sale.setDiscount(request.getDiscount());
            sale.setProducts(products);

            return saleRepository.save(sale);
        } else
            throw new RuntimeException(SALE_NOT_FOUND);
    }

    @Override
    public void deleteById(Long id) {
        if (!saleRepository.existsById(id))
            throw new RuntimeException(SALE_NOT_FOUND);
        saleRepository.deleteById(id);
    }

    @Override
    public Page<Sale> findByNameContaining(String name, Pageable pageable) {
        return saleRepository.findByNameContaining(name, pageable);
    }

    @Override
    public long count() {
        return saleRepository.count();
    }

    @Override
    public Optional<Sale> findById(Long id) {
        if (!saleRepository.existsById(id))
            throw new RuntimeException(SALE_NOT_FOUND);
        return saleRepository.findById(id);
    }
}
