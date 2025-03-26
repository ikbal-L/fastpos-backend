package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.dto.OrderDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImplOrderItemValidationTableIdExistIfOrderTypeEqualOnTable implements ConstraintValidator<OrderValidationTableIdExistIfOrderTypeEqualOnTable, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        boolean TableIdExistIfOrderTypeEqualOnTable = true;

        if (orderDto.getTableId() == null) {

            if (orderDto.getType() == OrderType.OnTable)
                TableIdExistIfOrderTypeEqualOnTable = false;

        }

        return TableIdExistIfOrderTypeEqualOnTable;
    }

}