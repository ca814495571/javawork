package com.cqfc.log.parser;

/**
 * @author: giantspider@126.com
 */
public interface LogParser {

    public LogMeta processLine(String line);

}
