package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderItemValidationOrderTypeNotEqualOnTable implements ConstraintValidator<OrderValidationOrderTypeNotEqualOnTable, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        boolean OrderTypeNotEqualOnTable = true;

        if (orderDto.getType() == OrderType.Delivery
                || orderDto.getType() == OrderType.InWaiting
                || orderDto.getType() == OrderType.TakeAway) {

            if (orderDto.getTableId() != null)
                OrderTypeNotEqualOnTable = false;

        }

        return OrderTypeNotEqualOnTable;
    }

}