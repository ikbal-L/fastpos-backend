package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplOrderItemValidationUnitPriceGreaterThanUnitPrice.class})
public @interface OrderItemValidationDiscountAmountGreaterThanTotal {

    String message() default MessageKeyConstants.ORDER_ITEM_DISCOUNT_AMOUNT_GREATER_THAN_TOTAL_ERROR;


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}