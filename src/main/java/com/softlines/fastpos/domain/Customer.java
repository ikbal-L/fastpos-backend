package com.softlines.fastpos.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softlines.fastpos.constants.MessageKeyConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @NotBlank(message = MessageKeyConstants.PRIVILEGE_NAME_VALIDATION_ERROR)
    String name;
    String mobile;

}
