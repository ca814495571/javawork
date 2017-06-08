package com.cqfc.log.analyzer;

import com.cqfc.log.parser.LogMeta;
import com.cqfc.log.parser.LogParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: giantspider@126.com
 */
public class Processor {

    Date startDate;
    Date endDate;
    LogParser logParser;
    RandomAccessFile randomAccessFile;
    private boolean detail = false;

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public void setLogParser(LogParser logParser) {
        this.logParser = logParser;
    }

    public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    public void process() {
        long startPosition = getStartPosition();
        Map<String, Integer> summary = new HashMap<String, Integer>();
        long cnt = 0;
        try {
            randomAccessFile.seek(startPosition);
            randomAccessFile.readLine();    //因为当前行可能是被截断了，跳过
            while (true) {
                String line = randomAccessFile.readLine();
                if (line == null)    break;                   //已到文件末尾
                LogMeta logMeta = logParser.processLine(line);
                if (logMeta.getDate().compareTo(startDate)>=0 && logMeta.getDate().compareTo(endDate)<0) {
                    cnt ++;
                    String status = logMeta.getStatus();
                    if (summary.containsKey(status)) {
                        summary.put(status, summary.get(status) + 1);
                    } else {
                        summary.put(status, 1);
                    }
                    if (detail) {
                        File targetFile = new File("logs/" + status);
                        FileUtils.write(targetFile, line+"\n", true);
                    }
                } else if (logMeta.getDate().compareTo(endDate)>0) {
                    break;  //当前行已经不在处理的时间范围之内
                } else {
                    continue;
                }
            }

            System.out.println("request: " + cnt);
            System.out.println("--------------");
            for (Map.Entry<String, Integer> row : summary.entrySet()) {
                System.out.println(row.getKey() + ": " + row.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //注意: start position返回的并不是精确位置
    private long getStartPosition() {
        long lo = 0;
        try {
            long hi = randomAccessFile.length();
            long p = (lo + hi)/2;
            while (p-lo > 4096) {
                randomAccessFile.seek(p);
                randomAccessFile.readLine();//因为当前行可能是被截断了，跳过
                String line = randomAccessFile.readLine();
                LogMeta logMeta = logParser.processLine(line);
                if (startDate.compareTo(logMeta.getDate())>0) {
                    lo = p;
                    p = (lo + hi)/2;
                } else {
                    hi = p;
                    p = (lo + hi)/2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lo;
    }

}
