package com.softlines.fastpos.validation.order;


import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderDtoValidationOrderItemCountLessThanOneImpl implements ConstraintValidator<OrderDtoValidationOrderItemCountLessThanOne, OrderDto> {

    @Override
    public boolean isValid(OrderDto value, ConstraintValidatorContext context) {
        return !(value.getOrderItems().isEmpty())||((value.getOrderItems().isEmpty())&& value.getState() == OrderState.Splitted);
    }
}
