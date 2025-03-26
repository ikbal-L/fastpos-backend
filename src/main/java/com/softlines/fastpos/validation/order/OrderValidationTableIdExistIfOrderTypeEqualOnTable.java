package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplOrderItemValidationTableIdExistIfOrderTypeEqualOnTable.class})
public @interface OrderValidationTableIdExistIfOrderTypeEqualOnTable {

    String message() default MessageKeyConstants.ORDER_TABLE_ID_EXIST_IF_ORDER_TYPE_EQUAL_ON_TABLE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}