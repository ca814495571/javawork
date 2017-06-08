package com.jami.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.cqfc.util.Configuration;

public class OrderLogger extends LotteryLogger {
	
	
	

	
	public static  String BACKFILE_PREFIX = Configuration.getConfigValue("backOrder_filePath").trim();
	public static final String LOG_FILE_PATH_SUBBIX = ".log";
	private static Map<String, OrderLogger> map = new ConcurrentHashMap<String, OrderLogger>();
	private String dbName = "";
	
	static {
		
		String log_path = System.getProperty("log_path");
		
		if(log_path!=null &&!"".equals(log_path)){
			
			BACKFILE_PREFIX = log_path +"/"+BACKFILE_PREFIX;
		}
		Log.run.debug("同步订单日志文件路径:"+BACKFILE_PREFIX);
	}
	
	private OrderLogger(String dbName) {
		super();
		this.dbName = dbName;
	}
	
	/**
	 * 根据传进来的名称生成动态文件名的日志文件。
	 * @param name
	 * @return 日志类
	 */
	public static OrderLogger getDynamicLogger(String name) {  
			if (map.containsKey(name)){
				return map.get(name);
			}
			OrderLogger logger = generateLogger(name);
			map.put(name, logger);
			return logger;
	}
	
    private static OrderLogger generateLogger(String name) {  
    	OrderLogger lotteryLogger = new OrderLogger(name); 
        // 生成新的Logger  
        // 如果已經有了一個Logger實例返回現有的  
    	lotteryLogger.logger = Logger.getLogger(name);  
        // 清空Appender。特別是不想使用現存實例時一定要初期化  
    	lotteryLogger.logger.removeAllAppenders();  
        // 設定Logger級別。  
    	lotteryLogger.logger.setLevel(Level.DEBUG);  
        // 設定是否繼承父Logger。  
        // 默認為true。繼承root輸出。  
        // 設定false後將不輸出root。  
    	lotteryLogger.logger.setAdditivity(true);  
        // 生成新的Appender  
    	RollingFileAppender appender = new RollingFileAppender();  
        PatternLayout layout = new PatternLayout();  
        // log的输出形式  
        String conversionPattern = "%d{yyyy-MM-dd HH:mm:ss} %m%n";  
        layout.setConversionPattern(conversionPattern); 
        appender.setLayout(layout);  
        appender.setMaxFileSize("100MB");
        appender.setMaxBackupIndex(10);
        // log输出路径  
        appender.setFile(BACKFILE_PREFIX+  name + LOG_FILE_PATH_SUBBIX);  
        // log的文字码  
        appender.setEncoding("UTF-8");  
        // true:在已存在log文件后面追加 false:新log覆盖以前的log  
        appender.setAppend(true);  
        // 适用当前配置  
        appender.activateOptions();  
        // 将新的Appender加到Logger中  
        lotteryLogger.logger.addAppender(appender);  
        return lotteryLogger;  
    }  
    
    public String getFilePath() {
		return BACKFILE_PREFIX+ this.dbName + LOG_FILE_PATH_SUBBIX;
	}
}
