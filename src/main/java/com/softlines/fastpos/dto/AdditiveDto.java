package com.softlines.fastpos.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

// TODO Remove additiveDto

@Data
public class AdditiveDto {

    long id;
    @NotBlank(message = "validation.error.additive.description")
    private String description;
    @NotBlank(message = "validation.error.additive.background")
    private String backgroundString;
    private int rank;

}
