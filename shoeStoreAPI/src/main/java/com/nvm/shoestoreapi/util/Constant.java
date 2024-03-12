package com.nvm.shoestoreapi.util;
import java.util.List;

public class Constant {
    // Error
    public static final String DUPLICATE_NAME = "DUPLICATE_NAME";
    public static final String DUPLICATE_SLUG = "DUPLICATE_SLUG";
    public static final String DUPLICATE_EMAIL = "DUPLICATE_EMAIL";
    public static final String DUPLICATE_PHONE = "DUPLICATE_PHONE";
    public static final String NAME_MAX_LENGTH_50 = "NAME_MAX_LENGTH_50";
    public static final String NAME_MAX_LENGTH_30 = "NAME_MAX_LENGTH_30";
    public static final String COLOR_MAX_LENGTH_20 = "NAME_MAX_LENGTH_20";
    public static final String ADDRESS_MAX_LENGTH_100 = "ADDRESS_MAX_LENGTH_100";
    public static final String EMAIL_NOT_VALID = "EMAIL_NOT_VALID";
    public static final String PHONE_NUMBER_MUST_HAVE_10_DIGITS = "PHONE_NUMBER_MUST_HAVE_10_DIGITS";
    public static final String ADDRESS_NOT_BLANK = "ADDRESS_NOT_BLANK";
    public static final String NAME_NOT_BLANK = "NAME_NOT_BLANK";
    public static final String COLOR_NOT_BLANK = "COLOR_NOT_BLANK";

    // Product
    public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    public static final String DELETE_PRODUCT_SUCCESS = "DELETE_PRODUCT_SUCCESS";
    public static final String DELETE_PRODUCT_IMAGE_SUCCESS = "DELETE_PRODUCT_IMAGE_SUCCESS";
    public static final String CHANGE_SWITCH_PRODUCT_SUCCESS = "CHANGE_SWITCH_PRODUCT_SUCCESS";
    public static final String UPDATE_PRODUCT_STATUS_SUCCESS = "UPDATE_PRODUCT_STATUS_SUCCESS";
    public static final String DELETE_PRODUCT_COLOR_SUCCESS = "DELETE_PRODUCT_COLOR_SUCCESS";
    public static final String PRODUCT_ID_NOT_DUPLICATE = "PRODUCT_ID_NOT_DUPLICATE";
    public static final String PRODUCT_COLOR_NOT_FOUND = "PRODUCT_COLOR_NOT_FOUND";
    public static final String PRODUCT_DETAILS_NOT_FOUND = "PRODUCT_DETAILS_NOT_FOUND";
    public static final String CANNOT_DELETE_PRODUCT_DETAILS = "CANNOT_DELETE_PRODUCT_DETAILS";
    public static final String DUPLICATE_PRODUCT_COLOR = "DUPLICATE_PRODUCT_COLOR";
    public static final String PRICE_MIN_0 = "PRICE_MIN_0";
    public static final String PRICE_MAX_1_BILLION = "PRICE_MAX_1_BILLION";
    public static final String QUANTITY_MUST_BE_GREATER_THAN_0 = "QUANTITY_MUST_BE_GREATER_THAN_0";
    public static final String QUANTITY_MAX_1000 = "QUANTITY_MAX_1000";
    public static final String PRODUCT_VALID = "PRODUCT_VALID";



    // Image
    public static final String IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND";
    public static final String IMAGE_NOT_VALID = "IMAGE_NOT_VALID";
    public static final String IMAGE_SIZE_TOO_LARGE_10MB = "IMAGE_SIZE_TOO_LARGE_10MB";

    public static final List<String> PRODUCT_SIZE = List.of("34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44");

    // Category
    public static final String CATEGORY_NOT_FOUND = "CATEGORY_NOT_FOUND";
    public static final String CANNOT_DELETE_CATEGORY = "CANNOT_DELETE_CATEGORY";
    public static final String DELETE_CATEGORY_SUCCESS = "DELETE_CATEGORY_SUCCESS";
    public static final String UPDATE_CATEGORY_STATUS_SUCCESS = "UPDATE_CATEGORY_STATUS_SUCCESS";

    // Brand
    public static final String BRAND_NOT_FOUND = "BRAND_NOT_FOUND";
    public static final String DELETE_BRAND_SUCCESS = "DELETE_BRAND_SUCCESS";
    public static final String UPDATE_BRAND_STATUS_SUCCESS = "UPDATE_BRAND_STATUS_SUCCESS";
    public static final String CHANGE_SWITCH_BRAND_SUCCESS = "CHANGE_SWITCH_BRAND_SUCCESS";
    public static final String CANNOT_DELETE_BRAND = "CANNOT_DELETE_BRAND";

    // Sale
    public static final String SALE_NOT_FOUND = "SALE_NOT_FOUND";
    public static final String DELETE_SALE_SUCCESS = "DELETE_SALE_SUCCESS";
    public static final String START_DATE_MUST_BE_BEFORE_END_DATE = "START_DATE_MUST_BE_BEFORE_END_DATE";
    public static final String START_DATE_AND_END_DATE_REQUIRED = "START_DATE_AND_END_DATE_REQUIRED";
    public static final String PRODUCT_ALREADY_IN_SALE = "PRODUCT_ALREADY_IN_SALE";
    // giảm giá > 0 và <= 100
    public static final String DISCOUNT_MUST_BE_LESS_THAN_100 = "DISCOUNT_MUST_BE_LESS_THAN_100";
    public static final String DISCOUNT_MUST_BE_GREATER_THAN_0 = "DISCOUNT_MUST_BE_GREATER_THAN_0";

    // Supplier
    public static final String SUPPLIER_NOT_FOUND = "SUPPLIER_NOT_FOUND";
    public static final String CANNOT_DELETE_SUPPLIER = "CANNOT_DELETE_SUPPLIER";
    public static final String DELETE_SUPPLIER_SUCCESS = "DELETE_SUPPLIER_SUCCESS";

    // Receipt
    public static final String RECEIPT_NOT_FOUND = "RECEIPT_NOT_FOUND";

    // Employee
    public static final String EMPLOYEE_NOT_FOUND = "EMPLOYEE_NOT_FOUND";

    // Account
    public static final String VERIFIED_SUCCESSFULLY = "VERIFIED_SUCCESSFULLY";
    public static final String RESET_PASSWORD_SUCCESSFULLY = "RESET_PASSWORD_SUCCESSFULLY";
    public static final String CHANGE_PASSWORD_SUCCESSFULLY = "CHANGE_PASSWORD_SUCCESSFULLY";
    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_NOT_FOUND";
    public static final String ACCOUNT_IS_LOCKED = "ACCOUNT_IS_LOCKED";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String THE_VERIFICATION_CODE_HAS_EXPIRED = "THE_VERIFICATION_CODE_HAS_EXPIRED";
    public static final String INVALID_VERIFICATION_CODE = "INVALID_VERIFICATION_CODE";
    public static final String ACCOUNT_ALREADY_VERIFIED = "ACCOUNT_ALREADY_VERIFIED";


    // Pagination
    public static final String PAGE_SIZE_DEFAULT = "5";
    public static final String USER_PAGE_SIZE_DEFAULT = "20";
    public static final String PAGE_NUMBER_DEFAULT = "1";
    public static final String SORT_BY_DEFAULT = "id";
    public static final String SORT_ORDER_DEFAULT = "DESC";

    // Store
    public static final String STORE_NAME = "Shoes Station";
    public static final String FROM_EMAIL = "manhnv291201@gmail.com";
    public static final String REQUEST_URL = "http://localhost:4200";

    // Role
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // Employee
    public static final String BIRTHDAY_NOT_BLANK = "BIRTHDAY_NOT_BLANK";
    public static final String STATUS_NOT_BLANK = "STATUS_NOT_BLANK";
    public static final String DELETE_EMPLOYEE_SUCCESS = "DELETE_EMPLOYEE_SUCCESS";
}
