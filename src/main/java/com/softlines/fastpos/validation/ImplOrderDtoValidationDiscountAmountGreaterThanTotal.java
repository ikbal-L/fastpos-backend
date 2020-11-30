package com.softlines.fastpos.validation;

import com.softlines.fastpos.dto.OrderDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderDtoValidationDiscountAmountGreaterThanTotal implements ConstraintValidator<OrderDtoValidationDiscountAmountGreaterThanTotal, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        return orderDto.getTotal() >= orderDto.getDiscountAmount();
    }

}