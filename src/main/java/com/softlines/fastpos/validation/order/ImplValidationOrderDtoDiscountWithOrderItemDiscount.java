package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImplValidationOrderDtoDiscountWithOrderItemDiscount implements ConstraintValidator<ValidationOrderDtoDiscountWithOrderItemDiscount, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        boolean ValidationOrderDtoDiscountWithOrderItemDiscount = true;

        if (orderDto.getDiscountAmount() > 0) {
            for (int i = 0; i < orderDto.getOrderItems().size(); i++) {
                if (orderDto.getOrderItems().get(i).getDiscountAmount() > 0) {
                    ValidationOrderDtoDiscountWithOrderItemDiscount = false;
                }
                break;
            }
        }
        return ValidationOrderDtoDiscountWithOrderItemDiscount;

    }
}