package com.jami.common.dao;

import org.springframework.util.Assert;import java.lang.ThreadLocal;

/**
 * @author: giantspider@126.com
 */

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<DataSourceEntry> contextHolder =
            new ThreadLocal<DataSourceEntry>();

    public static void setDataSourceEntry(DataSourceEntry dataSourceEntry) {
        Assert.notNull(dataSourceEntry, "dataSourceEntry cannot be null");
        contextHolder.set(dataSourceEntry);
    }

    public static DataSourceEntry getDataSourceEntry() {
        return (DataSourceEntry) contextHolder.get();
    }

    public static void clearDataSourceEntry() {
        contextHolder.remove();
    }

}
