package com.softlines.fastpos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Data
@NoArgsConstructor
public class DeliverymanDto extends PersonDto {

    @JsonProperty("Balance")
    float balance;
}
