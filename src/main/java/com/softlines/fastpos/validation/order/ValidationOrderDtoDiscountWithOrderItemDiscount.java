package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplValidationOrderDtoDiscountWithOrderItemDiscount.class})
public @interface ValidationOrderDtoDiscountWithOrderItemDiscount {

    String message() default MessageKeyConstants.ORDER_DISCOUNT_WITH_ORDER_ITEM_DISCOUNT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}