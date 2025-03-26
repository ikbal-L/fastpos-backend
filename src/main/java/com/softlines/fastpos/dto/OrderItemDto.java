package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.OrderItemState;
import com.softlines.fastpos.validation.order.OrderDtoValidationDiscountAmountGreaterThanTotal;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@OrderDtoValidationDiscountAmountGreaterThanTotal
public class OrderItemDto {

    @JsonProperty("Id")
    long id;

    @JsonProperty("UnitPrice")
    @Min(1)
    @NotNull
    double unitPrice;

    @JsonProperty("CustomPrice")
    Double customPrice;

    @JsonProperty("Quantity")
    @NotNull
    @Min(1)
    int quantity;

    @JsonProperty("Total")
    @NotNull
    @Min(1)
    double total;

    @JsonProperty("DiscountAmount")
    @Min(0)
    @NotNull
    double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    @Min(0)
    double totalDiscountAmount;


    @JsonProperty("ProductId")
    @NotNull
    Long productId;
    @JsonProperty("SplitFromOrderItemId")
    Long splitFromOrderItemId;

    @JsonProperty("ProductName")
    @NotNull
    String productName;

    @JsonProperty("OrderId")
    Long orderId;

    @JsonProperty("TimeStamp")
    Date timestamp;

    @NotNull
    @JsonProperty("State")
    OrderItemState state;

    @JsonProperty("OrderItemAdditives")
    Set<OrderItemAdditiveDto> orderItemAdditives;

}
