package com.softlines.fastpos.dto.mapping;

import com.softlines.fastpos.domain.DailyExpenseReport;
import com.softlines.fastpos.dto.DailyExpenseReportDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {PaymentMapper.class})
public interface DailyExpenseReportMapper {

    DailyExpenseReportDto toDailyExpenseReportDto(DailyExpenseReport dailyExpenseReport);
    List<DailyExpenseReportDto> toDailyExpenseReportDtos(List<DailyExpenseReport> dailyExpenseReports);
}
