package com.nvm.shoestoreapi.service.impl;

import com.nvm.shoestoreapi.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecifications {
    public static Specification<Product> filterProducts(
            String categorySlug,
            List<String> origins,
            List<String> brands,
            List<String> materials,
            List<String> shapes,
            List<Integer> timeWarranties,
            Double priceMin,
            Double priceMax) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("category").get("slug"), categorySlug));
            predicates.add(criteriaBuilder.equal(root.get("status"), true));

            if (brands != null && !brands.isEmpty()) {
                predicates.add(root.get("brand").get("name").in(brands));
            }
            if (materials != null && !materials.isEmpty()) {
                predicates.add(root.get("material").get("name").in(materials));
            }
            if (shapes != null && !shapes.isEmpty()) {
                predicates.add(root.get("shape").get("name").in(shapes));
            }
            if (origins != null && !origins.isEmpty()) {
                predicates.add(root.get("origin").get("name").in(origins));
            }
            if (timeWarranties != null && !timeWarranties.isEmpty()) {
                predicates.add(root.get("timeWarranty").in(timeWarranties));
            }
            if (Objects.nonNull(priceMin) && Objects.isNull(priceMax)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
            }
            if (Objects.isNull(priceMin) && Objects.nonNull(priceMax)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
            }
            if (Objects.nonNull(priceMin) && Objects.nonNull(priceMax)) {
                predicates.add(criteriaBuilder.between(root.get("price"), priceMin, priceMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> filter(
            List<String> brands,
            List<String> categories,
            Long priceMin,
            Long priceMax) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("enabled"), true));

            if (brands != null && !brands.isEmpty()) {
                predicates.add(root.get("brand").get("slug").in(brands));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").get("slug").in(categories));
            }
            if (Objects.nonNull(priceMin) && Objects.nonNull(priceMax)) {
                predicates.add(criteriaBuilder.between(root.get("price"), priceMin, priceMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Product> search(String name,
                                                List<String> originName,
                                                List<String> brandName,
                                                List<String> materialName,
                                                List<String> shapeName,
                                                List<Integer> timeWarranty,
                                                Double priceMin,
                                                Double priceMax) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();//?#{escape([0])}
            // tim kiem ma co ky tu dac biet su dung ham ?#{escape([0])}
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));

            predicates.add(criteriaBuilder.equal(root.get("status"), true));

            if (brandName != null && !brandName.isEmpty()) {
                predicates.add(root.get("brand").get("name").in(brandName));
            }
            if (materialName != null && !materialName.isEmpty()) {
                predicates.add(root.get("material").get("name").in(materialName));
            }
            if (shapeName != null && !shapeName.isEmpty()) {
                predicates.add(root.get("shape").get("name").in(shapeName));
            }
            if (originName != null && !originName.isEmpty()) {
                predicates.add(root.get("origin").get("name").in(originName));
            }
            if (timeWarranty != null && !timeWarranty.isEmpty()) {
                predicates.add(root.get("timeWarranty").in(timeWarranty));
            }
            if (Objects.nonNull(priceMin) && Objects.isNull(priceMax)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
            }
            if (Objects.isNull(priceMin) && Objects.nonNull(priceMax)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
            }
            if (Objects.nonNull(priceMin) && Objects.nonNull(priceMax)) {
                predicates.add(criteriaBuilder.between(root.get("price"), priceMin, priceMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
