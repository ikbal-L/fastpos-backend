package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.domain.DailyEarningsReport;
import com.softlines.fastpos.dto.filters.Filter;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class DailyEarningsReportsFilterService extends  FilterService<DailyEarningsReport, Filter> {
    @Override
    protected void initializePredicates() throws ParseException {

    }

    @Override
    protected void initializeCriteriaQuery() {

    }
}
