package com.softlines.fastpos.dto.mapping;


import com.softlines.fastpos.domain.Customer;
import com.softlines.fastpos.domain.Waiter;
import com.softlines.fastpos.dto.CustomerDto;
import com.softlines.fastpos.dto.WaiterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toCustomerDto(Customer customer);

    List<CustomerDto> toCustomerDTOs(List<Customer> customers);

    Customer toCustomer(CustomerDto customerDto);

}
