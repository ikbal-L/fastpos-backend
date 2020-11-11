package com.softlines.fastpos.dto;

import com.softlines.fastpos.domain.Descriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    long id;
    String name;
    String address;
    String serverLicenceKey;
    String username;
    String password;
    String pinCode;
    String phoneNumber;
    String backgroundString;
    boolean isActive;
    Descriptor descriptor;
//    List<Long> rolesId;
}
