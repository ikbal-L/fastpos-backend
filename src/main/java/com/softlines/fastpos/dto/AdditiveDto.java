package com.softlines.fastpos.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

// TODO Remove additiveDto

@Data
public class AdditiveDto {

    long id;
    @NotBlank(message = "description should not be empty")
    private String description;
    @NotBlank(message = "backgroundString should not be empty")
    private String backgroundString;
    private int rank;

}
