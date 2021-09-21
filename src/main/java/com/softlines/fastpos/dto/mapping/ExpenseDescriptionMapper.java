package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.ExpenseDescription;
import com.softlines.fastpos.dto.ExpenseDescriptionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseDescriptionMapper {

    ExpenseDescriptionDto toExpenseDescriptionDto(ExpenseDescription expense);

    ExpenseDescription toExpenseDescription(ExpenseDescriptionDto expenseDto);

    List<ExpenseDescriptionDto> toExpenseDescriptionDTOs(List<ExpenseDescription> expenses);
}
