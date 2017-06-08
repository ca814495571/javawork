package com.cqfc.log.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author: giantspider@126.com
 */
public class NginxParser implements LogParser {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);

    /**
     * nginx 日记格式:
     *  "$time_local" "$remote_addr" "$host" "$status" "$request" "$bytes_sent" "$request_time" "$arg_g_tk"  "$cookie_visitkey" "$upstream_addr" "$upstream_status" "$upstream_response_time" "$http_referer" "$http_user_agent" "$sent_http_location"
     * 其中 $time_local的格式为: "11/Dec/2014:18:56:26 +0800"
     */
    @Override
    public LogMeta processLine(String line) {
        LogMeta ret = null;
        String [] parts = line .split("\"");
        List<String> values = new ArrayList<String>();
        for (String part : parts) {
            if (part!=null && part.trim().length()>0)    values.add(part);
        }

        if (values.size()>6)  {
            String [] elems = values.get(0).split("\\s");
            String timestamp = elems[0];
            String status = values.get(3);
            ret = new LogMeta();
            try {
                ret.setDate(sdf.parse(timestamp));
            } catch (ParseException e) {
                System.out.println("[ERROR] parse timestamp:" + timestamp);
            }
            ret.setStatus(status);
        }
        return ret;
    }

}
