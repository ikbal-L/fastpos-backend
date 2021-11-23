package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.CashRegisterExpense;
import com.softlines.fastpos.dto.CashRegisterExpenseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CashRegisterExpenseMapper {

    CashRegisterExpenseDto toCashRegisterExpenseDto(CashRegisterExpense expense);

    CashRegisterExpense toCashRegisterExpense(CashRegisterExpenseDto expenseDto);

    List<CashRegisterExpenseDto> toCashRegisterExpenseDTOs(List<CashRegisterExpense> expenses);
}
