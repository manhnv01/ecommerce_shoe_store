package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.entity.Product;
import com.nvm.shoestoreapi.entity.ProductColor;
import com.nvm.shoestoreapi.entity.ProductDetails;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecifications {
    public static Specification<Product> filter(
            List<String> brands,
            List<String> categories,
            List<String> productSizes,
            String search,
            Long priceMin,
            Long priceMax) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("enabled"), true));

            // tìm kiếm tương đối theo tên sản phẩm
            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + search + "%"));
            }

            if (brands != null && !brands.isEmpty()) {
                predicates.add(root.get("brand").get("slug").in(brands));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").get("slug").in(categories));
            }
            if (Objects.nonNull(priceMin) && Objects.nonNull(priceMax)) {
                predicates.add(criteriaBuilder.between(root.get("price"), priceMin, priceMax));
            }
            if (productSizes != null && !productSizes.isEmpty()) {
                Join<Product, ProductColor> productColorJoin = root.join("productColors", JoinType.INNER);
                Join<ProductColor, ProductDetails> productDetailJoin = productColorJoin.join("productDetails", JoinType.INNER);
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.in(productDetailJoin.get("size")).value(productSizes),
                        criteriaBuilder.gt(productDetailJoin.get("quantity"), 0)
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
