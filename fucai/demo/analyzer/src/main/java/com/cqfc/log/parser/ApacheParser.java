package com.cqfc.log.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author: giantspider@126.com
 */
public class ApacheParser implements LogParser {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);

    /**
     * apache 日记格式:   %h %l %u %t \"%r\" %>s %b
     */
    @Override
    public LogMeta processLine(String line) {
        LogMeta ret = null;
        String [] parts = line .split("\"");
        if (parts.length>2) {
            ret = new LogMeta();
            String [] tmp1 = parts[0].trim().split("[\\[|\\s]");
            String timestamp = tmp1[tmp1.length-2];
            try {
                ret.setDate(sdf.parse(timestamp));
            } catch (ParseException e) {
                System.out.println("[ERROR] parse timestamp:" + timestamp);
            }
            String [] tmp2 = parts[2].trim().split("\\s");
            ret.setStatus(tmp2[0]);
        }
        return ret;
    }

}
