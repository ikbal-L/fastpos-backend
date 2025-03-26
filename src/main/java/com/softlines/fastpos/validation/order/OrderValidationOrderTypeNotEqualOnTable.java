package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplOrderItemValidationOrderTypeNotEqualOnTable.class})
public @interface OrderValidationOrderTypeNotEqualOnTable {

    String message() default MessageKeyConstants.TABLE_ID_MUST_EQUAL_NULL;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}