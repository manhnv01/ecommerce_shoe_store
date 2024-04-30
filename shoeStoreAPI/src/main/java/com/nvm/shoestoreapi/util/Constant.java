package com.nvm.shoestoreapi.util;
import com.nvm.shoestoreapi.dto.request.OrderDetailsRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public static final String PRODUCT_DETAILS_DUPLICATE = "PRODUCT_DETAILS_DUPLICATE";
    public static final String CANNOT_DELETE_PRODUCT_DETAILS = "CANNOT_DELETE_PRODUCT_DETAILS";
    public static final String DUPLICATE_PRODUCT_COLOR = "DUPLICATE_PRODUCT_COLOR";
    public static final String PRICE_MIN_0 = "PRICE_MIN_0";
    public static final String PRICE_MAX_1_BILLION = "PRICE_MAX_1_BILLION";
    public static final String QUANTITY_MUST_BE_GREATER_THAN_0 = "QUANTITY_MUST_BE_GREATER_THAN_0";
    public static final String QUANTITY_MAX_1000 = "QUANTITY_MAX_1000";
    public static final String PRODUCT_VALID = "PRODUCT_VALID";
    public static final String PRODUCT_QUANTITY_NOT_ENOUGH = "PRODUCT_QUANTITY_NOT_ENOUGH";


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
    public static final String GENDER_NOT_BLANK = "GENDER_NOT_BLANK";
    public static final String BIRTHDAY_NOT_BLANK = "BIRTHDAY_NOT_BLANK";
    public static final String STATUS_NOT_BLANK = "STATUS_NOT_BLANK";
    public static final String EMPLOYEE_AGE_LESS_THAN_18 = "EMPLOYEE_AGE_LESS_THAN_18";
    public static final String CITY_NOT_BLANK = "CITY_NOT_BLANK";
    public static final String DISTRICT_NOT_BLANK = "DISTRICT_NOT_BLANK";
    public static final String WARD_NOT_BLANK = "WARD_NOT_BLANK";
    public static final String ADDRESS_DETAIL_NOT_BLANK = "ADDRESS_DETAIL_NOT_BLANK";
    public static final String STOPPED_WORKING = "Đã nghỉ làm";
    public static final String WORKING = "Đang làm việc";

    // Account
    public static final String VERIFIED_SUCCESSFULLY = "VERIFIED_SUCCESSFULLY";
    public static final String SEND_EMAIL_SUCCESSFULLY = "SEND_EMAIL_SUCCESSFULLY";
    public static final String RESET_PASSWORD_SUCCESSFULLY = "RESET_PASSWORD_SUCCESSFULLY";
    public static final String CHANGE_PASSWORD_SUCCESSFULLY = "CHANGE_PASSWORD_SUCCESSFULLY";
    public static final String ACCOUNT_NOT_FOUND = "ACCOUNT_NOT_FOUND";
    public static final String ACCOUNT_IS_LOCKED = "ACCOUNT_IS_LOCKED";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String THE_VERIFICATION_CODE_HAS_EXPIRED = "THE_VERIFICATION_CODE_HAS_EXPIRED";
    public static final String INVALID_VERIFICATION_CODE = "INVALID_VERIFICATION_CODE";
    public static final String ACCOUNT_ALREADY_VERIFIED = "ACCOUNT_ALREADY_VERIFIED";
    public static final String PASSWORD_LENGTH_6_30 = "PASSWORD_LENGTH_6_30";
    public static final String PASSWORD_NOT_BLANK = "PASSWORD_NOT_BLANK";
    public static final String CODE_LENGTH_6 = "CODE_LENGTH_6";
    public static final String UNLOCK_ACCOUNT_SUCCESSFULLY = "UNLOCK_ACCOUNT_SUCCESSFULLY";
    public static final String LOCK_ACCOUNT_SUCCESSFULLY = "LOCK_ACCOUNT_SUCCESSFULLY";


    // Pagination
    public static final String PAGE_SIZE_DEFAULT = "5";
    public static final String USER_PAGE_SIZE_DEFAULT = "12";
    public static final String PAGE_NUMBER_DEFAULT = "1";
    public static final String SORT_BY_DEFAULT = "id";
    public static final String SORT_ORDER_DEFAULT = "DESC";

    // Store
    public static final String STORE_NAME = "Shoes Station";
    public static final String FROM_EMAIL = "manhnv291201@gmail.com";
    public static final String REQUEST_URL = "http://localhost:4200";

    // Role
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String FORBIDDEN = "FORBIDDEN";

    // Customer
    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER_NOT_FOUND";

    // Cart
    public static final String DELETE_CART_DETAILS_SUCCESS = "DELETE_CART_DETAILS_SUCCESS";
    public static final String CART_DETAILS_NOT_FOUND = "CART_DETAILS_NOT_FOUND";
    public static final String CART_IS_FULL = "CART_IS_FULL";

    // Order
    public static final String ORDER_NOT_FOUND = "ORDER_NOT_FOUND";
    public static final String PAYMENT_METHOD_NOT_BLANK = "PAYMENT_METHOD_NOT_BLANK";
    public static final String PAYMENT_STATUS_NOT_BLANK = "PAYMENT_STATUS_NOT_BLANK";
    public static final String ORDER_STATUS_NOT_BLANK = "ORDER_STATUS_NOT_BLANK";
    public static final String PRODUCTS_NOT_EMPTY = "PRODUCTS_NOT_EMPTY";
    public static final String ORDER_NOT_COMPLETED = "ORDER_NOT_COMPLETED";

    //Đơn hàng đã hoàn thành không thể cập nhật
    public static final String ORDER_COMPLETED_CANNOT_UPDATE = "ORDER_COMPLETED_CANNOT_UPDATE";
    //Đơn hàng đã hủy không thể cập nhật
    public static final String ORDER_CANCELLED_CANNOT_UPDATE = "ORDER_CANCELLED_CANNOT_UPDATE";
    // Đơn hàng đã hoàn trả không thể cập nhật
    public static final String ORDER_RETURNED_CANNOT_UPDATE = "ORDER_RETURNED_CANNOT_UPDATE";
    public static final String ORDER_TYPE_NOT_BLANK = "ORDER_TYPE_NOT_BLANK";

    // return product
    public static final String QUANTITY_RETURN_PRODUCT_MUST_BE_LESS_THAN_QUANTITY_ORDER = "QUANTITY_RETURN_PRODUCT_MUST_BE_LESS_THAN_QUANTITY_ORDER";
    public static final String RETURN_PRODUCT_NOT_BELONG_ORDER = "RETURN_PRODUCT_NOT_BELONG_ORDER";

    //status
    public static final String RETURN_PENDING = "RETURN_PENDING"; // chờ xử lý
    public static final String RETURN_APPROVED = "RETURN_APPROVED"; // đã xử lý
    public static final String RETURN_REJECTED = "RETURN_REJECTED"; // đã từ chối
    // RETURN_PRODUCT_NOT_FOUND
    public static final String RETURN_PRODUCT_NOT_FOUND = "RETURN_PRODUCT_NOT_FOUND";
    public static final String RETURN_PRODUCT_DETAILS_NOT_FOUND = "RETURN_PRODUCT_DETAILS_NOT_FOUND";
    public static final String RETURN_PRODUCT_STATUS_CANNOT_BE_CHANGED = "RETURN_PRODUCT_STATUS_CANNOT_BE_CHANGED";
    public static final String RETURN_PRODUCT_STATUS_INVALID = "RETURN_PRODUCT_STATUS_INVALID";
    public static final String RETURN_PRODUCT_PENDING_EXISTED = "RETURN_PRODUCT_PENDING_EXISTED";


    // Shipping
    public static final String GO_SHIP_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjJlNTlmZmRlY2ZhYzhhOTU5OTcwYWE2MWExNjEzMGMzMjAwMTc4NTJiNTRmNDM1Y2E1MDEzZjMzMmRmYTI2NGVhMTk4Y2JlNWE3MWEyNTc2In0.eyJhdWQiOiI4MCIsImp0aSI6IjJlNTlmZmRlY2ZhYzhhOTU5OTcwYWE2MWExNjEzMGMzMjAwMTc4NTJiNTRmNDM1Y2E1MDEzZjMzMmRmYTI2NGVhMTk4Y2JlNWE3MWEyNTc2IiwiaWF0IjoxNzEzMjEzNjA4LCJuYmYiOjE3MTMyMTM2MDgsImV4cCI6NDg2ODg4NzIwOCwic3ViIjoiMzI3MyIsInNjb3BlcyI6W119.F2E0Dvw-dzv0vwOp4OfRjpwnoqASRTx6vB87wKfzPr6Ah6r9Zhs-1TkX_D2Wo6eR9nRyfjNdLIzDkSFFBVa6vAkFRCGA6B0X3B3mmKiz23xSESjJgeUJwBoL2ai7OHOo28JM_FOj3mJG8q0YkFYHQVYg81ddptrHd1KdWYvkURWpIPDhggluNDCASOPmxQHM-atZTGcpMjXTJHDaY_YDpCO_zOb2S3TAOfY2a4k2KKT06k6QsNoXZ5arHXJKaVixsDr8-8jqBTmu2xIWZL414mzz60Sxs7JD1o6wLQNqjUFH50VoYz7ja5WkuRUf34rxiOVpFEtVMsgOk0GL8N24EjFzM-W2ORTZztizdTaO968VcrtGq7k_eCTZXC9JYqhrf-WXWBm50vTo1-xNOhHC-RpSSIQyukPTSqIQJfNlVCbospmXJS1KDMU-Y9f4b8FBdYr4ufZLZ3nBFc26TamwuCSpmmsYm8mB5T8QEilx5n1SYvct3Gcg0wWuKZOI2qiysfMbSy9IDOXxW6EY8TjhVugZMTwBksn8SZ1CnYNZIE-zMMESflmd2S4rOYTd4NQT6bJFr9eG2EToGo65mH_g4Lc7dqDT31069D2OaHtiUd6ovFj_hE7gB4jh1PzOxieAy_6cydeJo9cvuLhn5U9pFfDBElEJVNUp6mVADnzxS7k";

    public static String formatTime(long time) {
        Date commentDate = new Date(time);
        long diffInMillis = new Date().getTime() - commentDate.getTime();
        long secondsAgo = TimeUnit.SECONDS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        if (secondsAgo < 60) {
            return "Vừa xong";
        } else if (secondsAgo < 3600) {
            long minutesAgo = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return minutesAgo + " phút trước";
        } else if (secondsAgo < 86400) {
            long hoursAgo = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return hoursAgo + " giờ trước";
        } else if (secondsAgo < 604800) {
            long daysAgo = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return daysAgo + " ngày trước";
        } else if (secondsAgo < 31536000) {
            long diffInWeeks = diffInMillis / (7 * 24 * 60 * 60 * 1000);
            return diffInWeeks + " tuần trước";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(commentDate);

            int commentMonth = calendar.get(Calendar.MONTH);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

            int commentDate2 = calendar.get(Calendar.DATE);
            int currentDate = Calendar.getInstance().get(Calendar.DATE);

            int diffInYears = Calendar.getInstance().get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
            if (currentMonth < commentMonth || (currentMonth == commentMonth && currentDate < commentDate2)) {
                diffInYears--;
            }
            return diffInYears + " năm trước";
        }
    }

}
