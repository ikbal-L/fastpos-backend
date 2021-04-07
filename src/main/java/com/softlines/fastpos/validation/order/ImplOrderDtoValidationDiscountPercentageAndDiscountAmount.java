package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderDtoValidationDiscountPercentageAndDiscountAmount implements ConstraintValidator<OrderDtoValidationDiscountPercentageAndDiscountAmount, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
         if (orderDto.getOrderItems().isEmpty()) return true;
         return (orderDto.getDiscountAmount() * 100 / orderDto.getTotal()
                 == orderDto.getDiscountPercentage());

    }

}