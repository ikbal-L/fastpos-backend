package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderDtoValidationCalculationNewTotalNotCorrect implements ConstraintValidator<OrderValidationCalculationNewTotalNotCorrect, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        return (orderDto.getTotal() - orderDto.getDiscountAmount()) == orderDto.getNewTotal();
    }

}