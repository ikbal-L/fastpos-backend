package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImplOrderDtoValidationCalculationNewTotalNotCorrect implements ConstraintValidator<OrderValidationCalculationNewTotalNotCorrect, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        return (orderDto.getTotal() - orderDto.getTotalDiscountAmount()) == orderDto.getNewTotal();
    }

}