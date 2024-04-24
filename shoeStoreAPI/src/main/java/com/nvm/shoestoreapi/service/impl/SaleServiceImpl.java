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

import java.util.*;

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
        if (saleRequest.getStartDate() == null || saleRequest.getEndDate() == null) {
            throw new RuntimeException(START_DATE_AND_END_DATE_REQUIRED);
        }
        if (saleRequest.getStartDate().after(saleRequest.getEndDate())) {
            throw new RuntimeException(START_DATE_MUST_BE_BEFORE_END_DATE);
        }

        // Kiểm tra ngày bắt đầu và kết thúc có chồng lấp với các sale hiện tại không
        List<Sale> overlappingSales = getOverlappingSales(saleRequest);

        Set<Long> uniqueProductIds = checkProductIdsDuplicates(saleRequest.getProductIds());

        // Kiểm tra các product có tồn tại không
        List<Product> products = getProducts(saleRequest.getProductIds());

        checkProductAlreadyInSale(overlappingSales, uniqueProductIds);

        Sale sale = new Sale();
        sale.setName(saleRequest.getName().trim());
        sale.setStartDate(setStartDate(saleRequest));
        sale.setEndDate(setEndDate(saleRequest));
        sale.setDiscount(saleRequest.getDiscount());
        sale.setProducts(products);
        return saleRepository.save(sale);
    }

    @Override
    public Sale update(SaleRequest saleRequest) {
        Optional<Sale> existingSale = saleRepository.findById(saleRequest.getId());
        if (existingSale.isPresent()) {
            Sale sale = existingSale.get();
            if (!sale.getName().equals(saleRequest.getName()) && saleRepository.existsByName(saleRequest.getName()))
                throw new RuntimeException(DUPLICATE_NAME);
            if (saleRequest.getStartDate() == null || saleRequest.getEndDate() == null) {
                throw new RuntimeException(START_DATE_AND_END_DATE_REQUIRED);
            }
            if (saleRequest.getStartDate().after(saleRequest.getEndDate())) {
                throw new RuntimeException(START_DATE_MUST_BE_BEFORE_END_DATE);
            }

            List<Sale> overlappingSales = getOverlappingSales(saleRequest);

            Set<Long> uniqueProductIds = checkProductIdsDuplicates(saleRequest.getProductIds());

            List<Product> products = getProducts(saleRequest.getProductIds());

            overlappingSales.remove(sale);

            checkProductAlreadyInSale(overlappingSales, uniqueProductIds);

            existingSale.get().getProducts().clear();

            sale.setName(saleRequest.getName().trim());
            sale.setStartDate(setStartDate(saleRequest));
            sale.setEndDate(setEndDate(saleRequest));
            sale.setDiscount(saleRequest.getDiscount());
            sale.setProducts(products);

            return saleRepository.save(sale);
        } else
            throw new RuntimeException(SALE_NOT_FOUND);
    }

    private Set<Long> checkProductIdsDuplicates(List<Long> productIds) {
        Set<Long> uniqueProductIds = new HashSet<>(productIds);
        if (uniqueProductIds.size() < productIds.size()) {
            throw new RuntimeException(PRODUCT_ID_NOT_DUPLICATE);
        }
        return uniqueProductIds;
    }

    private Date setStartDate(SaleRequest saleRequest) {
        Date startDate = saleRequest.getStartDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private Date setEndDate(SaleRequest saleRequest) {
        Date endDate = saleRequest.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    // Lay ra cac sale co ngay bat dau hoac ket thuc nam trong khoang thoi gian cua saleRequest
    private List<Sale> getOverlappingSales(SaleRequest saleRequest) {
        return saleRepository.findByStartDateBetweenOrEndDateBetweenOrStartDateLessThanEqualAndEndDateGreaterThanEqual(
                saleRequest.getStartDate(), saleRequest.getEndDate(),
                saleRequest.getStartDate(), saleRequest.getEndDate(),
                saleRequest.getStartDate(), saleRequest.getEndDate());
    }

    private List<Product> getProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new RuntimeException(PRODUCT_NOT_FOUND);
        }
        return products;
    }

    private void checkProductAlreadyInSale(List<Sale> overlappingSales, Set<Long> uniqueProductIds) {
        if (!overlappingSales.isEmpty()) {
            for (Sale existingSale : overlappingSales) {
                for (Product existingProduct : existingSale.getProducts()) {
                    if (uniqueProductIds.contains(existingProduct.getId())) {
                        throw new RuntimeException(PRODUCT_ALREADY_IN_SALE);
                    }
                }
            }
        }
    }

    @Override
    public void validateProductInSale(SaleRequest saleRequest) {
        if (saleRequest.getStartDate() == null || saleRequest.getEndDate() == null) {
            throw new RuntimeException(START_DATE_AND_END_DATE_REQUIRED);
        }
        if (saleRequest.getStartDate().after(saleRequest.getEndDate())) {
            throw new RuntimeException(START_DATE_MUST_BE_BEFORE_END_DATE);
        }
        List<Sale> overlappingSales = getOverlappingSales(saleRequest);
        Set<Long> uniqueProductIds = checkProductIdsDuplicates(saleRequest.getProductIds());

        if (saleRequest.getId() != null) {
            Optional<Sale> sale = saleRepository.findById(saleRequest.getId());
            sale.ifPresent(overlappingSales::remove);
        }
        checkProductAlreadyInSale(overlappingSales, uniqueProductIds);
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
