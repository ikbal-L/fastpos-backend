package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderItemValidationTableIdExistIfOrderTypeEqualOnTable implements ConstraintValidator<OrderValidationTableIdExistIfOrderTypeEqualOnTable, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        boolean TableIdExistIfOrderTypeEqualOnTable = true;

        if (orderDto.getType() == OrderType.OnTable) {

            if (orderDto.getTableId() == null)
                TableIdExistIfOrderTypeEqualOnTable = false;

        }

        return TableIdExistIfOrderTypeEqualOnTable;
    }

}