package com.softlines.fastpos.validation;

import com.softlines.fastpos.dto.OrderDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderValidationDiscountAmountGreaterThanTotal implements ConstraintValidator<OrderValidationDiscountAmountGreaterThanTotal, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        return orderDto.getTotal() >= orderDto.getDiscountAmount();
    }

}