package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplOrderItemsDtoValidationDiscountPercentageAndDiscountAmount.class})
public @interface OrderItemsDtoValidationDiscountPercentageAndDiscountAmount {

    String message() default MessageKeyConstants.ORDER_ITEMS_DISCOUNT_PERCENTAGE_NOT_EQUAL_DISCOUNT_AMOUNT_ERROR;


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}