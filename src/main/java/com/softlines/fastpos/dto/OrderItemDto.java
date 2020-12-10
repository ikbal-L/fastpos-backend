package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.domain.OrderItemState;
import com.softlines.fastpos.validation.order.OrderDtoValidationDiscountAmountGreaterThanTotal;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@OrderDtoValidationDiscountAmountGreaterThanTotal
public class OrderItemDto {

    @JsonProperty("Id")
    Long id;

    @JsonProperty("UnitPrice")
    @Min(1)
    @NotNull
    Double unitPrice;

    @JsonProperty("Quantity")
    @Min(1)
    @NotNull
    Integer quantity;

    @JsonProperty("Total")
    @NotNull
    Double total;

    @JsonProperty("DiscountAmount")
    Double discountAmount;

    @JsonProperty("TotalDiscountAmount")
    Double totalDiscountAmount;

    @JsonProperty("DiscountPercentage")
    Double discountPercentage;

    @JsonProperty("ProductId")
    @NotNull
    Long productId;

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

//<<<<<<< HEAD
    @JsonProperty("OrderItemAdditives")
    Set<OrderItemAdditiveDto> orderItemAdditives;
//=======
//    @JsonProperty("OrderItemAdditive")
//    Set<OrderItemAdditiveDto> orderItemAdditives;
//>>>>>>> orderItemAdditive


}
