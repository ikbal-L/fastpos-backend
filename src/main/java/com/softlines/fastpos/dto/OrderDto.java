package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.domain.Deliveryman;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.domain.Waiter;
import com.softlines.fastpos.validation.order.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@OrderDtoValidationDiscountAmountGreaterThanTotal()
@OrderItemValidationDiscountAmountGreaterThanTotal
@OrderValidationTableIdExistIfOrderTypeEqualOnTable
@OrderValidationOrderTypeNotEqualOnTable
@OrderValidationTotalGreaterThanNewTotal
@OrderValidationOrderItemsCountLessThanOne
@OrderDtoValidationDiscountPercentageAndDiscountAmount
@OrderItemsDtoValidationDiscountPercentageAndDiscountAmount
@OrderValidationCalculationNewTotalNotCorrect
@ValidationOrderDtoDiscountWithOrderItemDiscount
public class OrderDto {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("BuyerId")
    String buyerId;

    @JsonProperty("OrderTime")
    Date orderTime;

    @JsonProperty("ElapsedTime")
    @NotNull
    LocalTime elapsedTime;

    @JsonProperty("Total")
    @Min(value = 0, message = MessageKeyConstants.ORDER_RETURNED_AMOUNT_VALIDATION_ERROR)
    @NotNull
    double total;

    @JsonProperty("SplittedFromId")
    int splittedFromId;

    @JsonProperty("NewTotal")
    @Min(0)
    double newTotal;

    @JsonProperty("DiscountAmount")
    @Min(0)
    @NotNull
    double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    @Min(0)
    double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")
    @Range(min = 0, max = 100)
    double discountPercentage;

    @JsonProperty("GivenAmount")
    @Min(0)
    double givenAmount;

    @JsonProperty("ReturnedAmount")
    @Max(value = 0, message = MessageKeyConstants.ORDER_RETURNED_AMOUNT_VALIDATION_ERROR)
    double returnedAmount;

    @JsonProperty("State")
    OrderState state;

    @JsonProperty("Type")
    OrderType type;

    @JsonProperty("OrderItems")
    @Size(min = 1)
    List<OrderItemDto> orderItems;

    @JsonProperty("TableId")
    Long tableId;

    @JsonProperty("DeliverymanId")
    Long deliverymanId;

    @JsonProperty("WaiterId")
    Long waiterId;


}





