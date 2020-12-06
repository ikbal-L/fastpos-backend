package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.constants.MessageKeyConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ImplOrderDtoValidationOrderItemCountLessThanOne.class})
public @interface OrderValidationOrderItemsCountLessThanOne {

    String message() default MessageKeyConstants.ORDER_ORDER_ITEMS_COUNT_LESS_THAN_ONE;


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}