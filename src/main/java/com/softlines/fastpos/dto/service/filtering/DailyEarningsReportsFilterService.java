package com.softlines.fastpos.dto.service.filtering;

import com.softlines.fastpos.domain.DailyEarningsReport;
import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.filters.Filter;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class DailyEarningsReportsFilterService extends  FilterService<DailyEarningsReport, Filter> {
    @Override
    protected void initializePredicates() throws ParseException {
        this.root = criteriaQuery.from(DailyEarningsReport.class);
        this.entityClass = DailyEarningsReport.class;
    }

    @Override
    protected void initializeCriteriaQuery() {
        this.criteriaQuery = criteriaBuilder.createQuery(DailyEarningsReport.class);
    }
}
