package com.softlines.fastpos.dbconfig.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Long determineCurrentLookupKey() {
        return CustomContextHolder.getId();
    }
}