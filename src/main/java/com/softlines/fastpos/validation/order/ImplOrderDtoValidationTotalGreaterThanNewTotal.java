package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImplOrderDtoValidationTotalGreaterThanNewTotal implements ConstraintValidator<OrderValidationTotalGreaterThanNewTotal, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {

        return orderDto.getTotal() >= orderDto.getNewTotal();
    }

}