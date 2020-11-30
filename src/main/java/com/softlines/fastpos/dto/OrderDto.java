package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.validation.order.OrderItemValidationDiscountAmountGreaterThanTotal;
import com.softlines.fastpos.validation.order.OrderDtoValidationDiscountAmountGreaterThanTotal;
import com.softlines.fastpos.validation.order.OrderValidationTotalGreaterThanNewTotal;
import lombok.*;

import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@OrderDtoValidationDiscountAmountGreaterThanTotal
@OrderItemValidationDiscountAmountGreaterThanTotal


@OrderValidationTotalGreaterThanNewTotal
public class OrderDto {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("BuyerId")
    String buyerId;

    @JsonProperty("OrderTime")
    Date orderTime;

    @JsonProperty("ElapsedTime")
    LocalTime elapsedTime;

    @JsonProperty("Total")
    @Min(value = 0, message = MessageKeyConstants.ORDER_RETURNED_AMOUNT_VALIDATION_ERROR)
    @NotNull
    Double total;

    @JsonProperty("SplittedFromId")
    int splittedFromId;

    @JsonProperty("NewTotal")
    @Min(0)
    double newTotal;

    @JsonProperty("DiscountAmount")
    @Min(0)

    Double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    @Min(0)

    Double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")
    @Range(min = 0, max = 100)

    Double discountPercentage;

    @JsonProperty("GivenAmount")
    @Min(0)

    Double givenAmount;

    @JsonProperty("ReturnedAmount")
    @Min(value = 0, message = MessageKeyConstants.ORDER_TOTAL_VALIDATION_ERROR)
    Double returnedAmount;

    @JsonProperty("ProductsVisibility")
    boolean productsVisibility;

    @JsonProperty("AdditivesVisibility")
    boolean additivesVisibility;

    @JsonProperty("Orderstate")
    OrderState orderstate;

    @JsonProperty("Type")
    OrderType type;

    @JsonProperty("OrderItems")
    List<OrderItemDto> orderItems;

    @JsonProperty("TableId")
    Long tableId;

    private boolean deleted = false;

}





