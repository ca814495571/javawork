package com.cqfc.log.parser;

import java.util.Date;

/**
 * @author: giantspider@126.com
 */
public class LogMeta {

    private Date date;
    private String status;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LogMeta{" +
                "date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
