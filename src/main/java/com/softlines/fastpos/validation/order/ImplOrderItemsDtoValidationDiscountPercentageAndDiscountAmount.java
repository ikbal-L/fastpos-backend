package com.softlines.fastpos.validation.order;

import com.softlines.fastpos.dto.OrderDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImplOrderItemsDtoValidationDiscountPercentageAndDiscountAmount implements ConstraintValidator<OrderItemsDtoValidationDiscountPercentageAndDiscountAmount, OrderDto> {

    public boolean isValid(OrderDto orderDto, ConstraintValidatorContext context) {
        boolean orderItemsValidationPercentageDiscountAndDiscountAmount = true;

        for (int i = 0; i < orderDto.getOrderItems().size(); i++) {
            if (!(orderDto.getOrderItems().get(i).getDiscountAmount() * 100 / orderDto.getOrderItems().get(i).getTotal()
                    == orderDto.getOrderItems().get(i).getDiscountPercentage()))
                orderItemsValidationPercentageDiscountAndDiscountAmount = false;
            break;
        }

        return orderItemsValidationPercentageDiscountAndDiscountAmount;

    }

}