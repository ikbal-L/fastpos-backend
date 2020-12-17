package com.softlines.fastpos.constants;

public class MessageKeyConstants {

    ///// ADDITIVE VALIDATION
    public static final String ADDITIVE_BACKGROUND_STRING_VALIDATION_ERROR = "validation.error.additive.backgroundString";
    public static final String ADDITIVE_DESCRIPTION_VALIDATION_ERROR = "validation.error.additive.description";

    ///// USER VALIDATION
    public static final String USER_USERNAME_VALIDATION_ERROR = "validation.user.error.username";
    public static final String USER_PASSWORD_VALIDATION_ERROR = "validation.user.error.password";

    ///// ROLE VALIDATION
    public static final String ROLE_NAME_VALIDATION_ERROR = "validation.role.error.name";

    ///// PRIVILEGE VALIDATION
    public static final String PRIVILEGE_NAME_VALIDATION_ERROR = "validation.privilege.error.name";

    ///// ORDER VALIDATION
    public static final String ORDER_TOTAL_VALIDATION_ERROR = "validation.order.error.total";
    public static final String ORDER_RETURNED_AMOUNT_VALIDATION_ERROR = "validation.order.error.returnedAmount";
    public static final String ORDER_TABLE_ID_EXIST_IF_ORDER_TYPE_EQUAL_ON_TABLE= "validation.order.error.tableId_must_equal_null";
    public static final String ORDER_ORDER_ITEMS_COUNT_LESS_THAN_ONE = "validation.order.error.orderItems_count_less_than_one";
    public static final String ORDER_DISCOUNT_AMOUNT_GREATER_THAN_TOTAL_ERROR = "validation.order.error.discountAmount_greater_than_total";
    public static final String ORDER_DISCOUNT_PERCENTAGE_NOT_EQUAL_DISCOUNT_AMOUNT_ERROR = "validation.order.error.discountPercentage_not_equal_discountAmount";
    public static final String ORDER_TOTAL_GREATER_THAN_NEW_TOTAL_ERROR = "validation.order.error.total_greater_than_new_total";
    public static final String ORDER_NEW_TOTAL_CALCULATION_NOT_CORRECT_ERROR = "validation.order.error.new_total_calculation_not_correct";
    public static final String ORDER_DISCOUNT_WITH_ORDER_ITEM_DISCOUNT = "validation.order.error.order_discount_with_orderItem_discount";

    /////  ORDER ITEMS VALIDATION
    public static final String ORDER_ITEM_DISCOUNT_AMOUNT_GREATER_THAN_TOTAL_ERROR = "validation.order_item.error.discountAmount_greater_than_total";
    public static final String ORDER_ITEMS_DISCOUNT_PERCENTAGE_NOT_EQUAL_DISCOUNT_AMOUNT_ERROR = "validation.order_items.error.discountPercentage_not_equal_discountAmount";

    ///// TABLE ITEMS VALIDATION
    public static final String TABLE_ID_MUST_EQUAL_NULL= "validation.order.error.tableId_must_equal_null";

    //// PRODUCT VALIDATION
    public static final String PRODUCT_NAME_VALIDATION_ERROR = "validation.product.error.name";
    public static final String PRODUCT_IF_CATEGORY_ID_EQUAL_NULL_RANK_MUST_EQUAL_NULL_VALIDATION_ERROR = "validation.product.error.if_categoryId_equal_null_rank_must_equal_null";

    //// CATEGORY VALIDATION
    public static final String CATEGORY_BACKGROUND_STRING_VALIDATION_ERROR = "validation.category.error.backgroundString";
    public static final String CATEGORY_NAME_VALIDATION_ERROR = "validation.category.error.backgroundString";
    public static final String CATEGORY_IF_RANK_EQUAL_NULL_PRODUCTS_IDS_MUST_EQUAL_NULL_VALIDATION_ERROR = "validation.product.error.if_rank_equal_null_than_productsIds_must_equal_null";

    //// CUSTOMER VALIDATION
    public static final String CUSTOMER_NAME_VALIDATION_ERROR = "validation.customer.error.name";
    public static final String CUSTOMER_MOBILE_VALIDATION_ERROR = "validation.customer.error.mobile";

    //// PERSON VALIDATION
    public static final String PERSON_NAME_VALIDATION_ERROR = "validation.person.error.name";
    public static final String PERSON_MOBILE_VALIDATION_ERROR = "validation.person.error.mobile";

}

