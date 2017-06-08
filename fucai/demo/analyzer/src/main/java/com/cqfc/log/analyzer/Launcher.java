package com.cqfc.log.analyzer;

import com.cqfc.io.BufferedRandomAccessFile;
import com.cqfc.log.parser.ApacheParser;
import com.cqfc.log.parser.LogParser;
import com.cqfc.log.parser.NginxParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 参数：
 *    -mode         nginx || apache || tomcat
 *    -logfile      日记文件
 *    -start        起始时间(统计时间段)
 *    -end          截止时间
 *    -date         统计某一天
 *    -detail       生成各个状态的明细
 *
 * 使用实例：
 *    -mode nginx -logfile /data/nginx/log/access.log -date 20141212 -detail=true
 *    -mode nginx -logfile /data/nginx/log/access.log -start 20141212:00:00:00 -end 20141213:00:00:00 -detail=true
 *
 * @author: giantspider@126.com
 */
public class Launcher {
    @Option(name="-mode", metaVar="<mode>", usage="nginx/apache/tomcat")
    private String mode;

    @Option(name="-logfile", metaVar="<logfile>", usage="/path/to/logfile")
    private String logfile;

    @Option(name="-detail", metaVar="<detail>", usage="true or false")
    private String detail;

    @Option(name="-start", metaVar="<start>", usage="start time")
    private String start;

    @Option(name="-end", metaVar="<end>", usage="end time")
    private String end;

    @Option(name="-date", metaVar="<date>", usage="specific date(like 20141212)")
    private String date;

    public void printUsage(){
        CmdLineParser parser = new CmdLineParser(this);
        System.out.println("usage: " + Launcher.class.getCanonicalName());
        parser.printUsage(System.out);
    }

    private static final SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyyMMdd:HH:mm:ss", Locale.ENGLISH);
    public void doMain(String[] args) throws Exception {
        CmdLineParser cliParser = new CmdLineParser(this);
        cliParser.setUsageWidth(80);
        try {
            cliParser.parseArgument(args);
        } catch (CmdLineException e) {
            e.printStackTrace();
        }

        if (args.length==0) {
            printUsage();
            return;
        }

        if (mode==null) {
            System.out.println("missing argument: -mode");
            System.exit(-1);
        }

        boolean flag = false;
        if (detail!=null && Boolean.valueOf(detail))    flag = true;

        LogParser parser = null;
        if (mode.equals("nginx")) {
            parser = new NginxParser();
        } else if (mode.equals("apache")) {
            parser = new ApacheParser();
        } else if (mode.equals("tomcat")) {
            System.out.println("tomcat log not support yet... ");
            return;
        } else {
            System.out.println("illegal argument: " + mode);
            return;
        }

        Date startDate = inputFormatter.parse(start);
        Date endDate = inputFormatter.parse(end);

        BufferedRandomAccessFile bufferedRandomAccessFile = new BufferedRandomAccessFile(logfile, "r");
        Processor processor = new Processor();
        processor.setStartDate(startDate);
        processor.setEndDate(endDate);
        processor.setDetail(flag);
        processor.setRandomAccessFile(bufferedRandomAccessFile);
        processor.setLogParser(parser);
        processor.process();
        bufferedRandomAccessFile.close();
    }

    public static void main(String[] args) throws Exception {
        new Launcher().doMain(args);
    }
}

