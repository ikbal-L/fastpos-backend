package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderItemValidationUnitPriceGreaterThanUnitPrice implements ConstraintValidator<OrderItemValidationDiscountAmountGreaterThanTotal, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        boolean validationDiscountAmountGreaterThanUnitPrice = true;

        for (int i = 0; i < orderDto.getOrderItems().size(); i++) {
            if (orderDto.getOrderItems().get(i).getDiscountAmount()
                    > orderDto.getOrderItems().get(i).getTotal()) {
                validationDiscountAmountGreaterThanUnitPrice = false;
            }
            break;
        }

        return validationDiscountAmountGreaterThanUnitPrice;
    }

}