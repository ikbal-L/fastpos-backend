package com.softlines.fastpos.validation;

import com.softlines.fastpos.constants.MessageKeyConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConfirmValidationDiscountAmount.class})
public @interface ValidationDiscountAmount {

    String message() default MessageKeyConstants.ORDER_DISCOUNT_AMOUNT_GREATER_THAN_TOTAL_ERROR;


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}