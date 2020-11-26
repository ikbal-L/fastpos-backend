package com.softlines.fastpos.constants;

public class MessageKeyConstants {

    ///// ADDITIVE VALIDATION
    public static final String ADDITIVE_BACKGROUND_VALIDATION_ERROR = "validation.error.additive.background";
    public static final String ADDITIVE_DESCRIPTION_VALIDATION_ERROR = "validation.error.additive.description";

    ///// USER VALIDATION
    public static final String USER_USERNAME_VALIDATION_ERROR = "validation.user.error.username";
    public static final String USER_PASSWORD_VALIDATION_ERROR = "validation.user.error.password";

    ///// ROLE VALIDATION
    public static final String ROLE_NAME_VALIDATION_ERROR = "validation.role.error.name";

    ///// PRIVILEGE VALIDATION
    public static final String PRIVILEGE_NAME_VALIDATION_ERROR = "validation.privilege.error.name";

    ///// ORDER VALIDATION
    public static final String ORDER_TOTAL_VALIDATION_ERROR = "validation.order : total value must be greater than or equal to 0";
    public static final String ORDER_RETURNED_AMOUNT_VALIDATION_ERROR = "validation.order : returnedAmount value must be greater than or equal to 0";

    public static final String ORDER_DISCOUNT_AMOUNT_GREATER_THAN_TOTAL_ERROR = "validation.order : discountAmount greater than total";

    ///// PRODUCT VALIDATION
    public static final String PRODUCT_NAME_VALIDATION_ERROR = "validation.product.error.name";

}

