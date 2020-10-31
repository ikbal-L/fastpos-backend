package com.softlines.fastpos.dto;

import lombok.Data;
import java.util.List;

@Data
public class RestaurentDto {

    long id;
    String name;
    String address;
    String serverLicenceKey;
    //    User owner;
    List<Long> annexesId;

}
