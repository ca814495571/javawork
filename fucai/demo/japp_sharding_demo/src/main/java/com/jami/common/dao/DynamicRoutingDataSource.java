package com.jami.common.dao;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;import java.lang.Object;import java.lang.Override;

/**
 * @author: giantspider@126.com
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceEntry();
    }

}
