package com.jami.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private Map<Object, Object> dataSources = new HashMap<Object, Object>();

//	String key = "";
	@Override
	protected Object determineCurrentLookupKey() {
		
		return DataSourceContextHolder.getDataSourceType();
	}

	@Override
	public void setDataSourceLookup(DataSourceLookup dataSourceLookup) {

		super.setDataSourceLookup(dataSourceLookup);

	}

	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {

		super.setDefaultTargetDataSource(defaultTargetDataSource);

	}

	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {

		super.setTargetDataSources(targetDataSources);
		super.afterPropertiesSet();

	}



}
