package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.softlines.fastpos.constants.MessageKeyConstants;
import com.softlines.fastpos.domain.OrderState;
import com.softlines.fastpos.domain.OrderType;
import com.softlines.fastpos.validation.order.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Data
@Builder
@OrderDtoValidationDiscountAmountGreaterThanTotal()
@OrderItemValidationDiscountAmountGreaterThanTotal
@OrderValidationTableIdExistIfOrderTypeEqualOnTable
@OrderValidationOrderTypeNotEqualOnTable
@OrderValidationTotalGreaterThanNewTotal
@OrderDtoValidationDiscountPercentageAndDiscountAmount
@OrderItemsDtoValidationDiscountPercentageAndDiscountAmount
@OrderValidationCalculationNewTotalNotCorrect
@ValidationOrderDtoDiscountWithOrderItemDiscount
@OrderDtoValidationOrderItemCountLessThanOne
public class OrderDto /*extends SyncDto*/ {

    @JsonProperty("Id")
    @Min(0)
    long id;

    @JsonProperty("OrderNumber")
    long orderNumber;

    @JsonProperty("CustomerId")
    String customerId;

    @JsonProperty("OrderTime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime orderTime;

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
    List<OrderItemDto> orderItems;

    @JsonProperty("TableId")
    Long tableId;

    @JsonProperty("DeliverymanId")
    Long deliverymanId;

    @JsonProperty("WaiterId")
    Long waiterId;

    @JsonProperty("IsLocked")
    protected boolean isLocked = false;

    @JsonProperty("LockedBy")
    protected String lockedBy;


}





