package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplOrderDtoValidationCalculationNewTotalNotCorrect.class})
public @interface OrderValidationCalculationNewTotalNotCorrect {

    String message() default MessageKeyConstants.ORDER_NEW_TOTAL_CALCULATION_NOT_CORRECT_ERROR;


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}