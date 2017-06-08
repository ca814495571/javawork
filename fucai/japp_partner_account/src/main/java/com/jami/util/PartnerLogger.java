package com.jami.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import com.cqfc.util.ConstantsUtil;

public class PartnerLogger extends LotteryLogger {
	private static String LOG_FILE_PATH_PREFIX = ConstantsUtil.LOG_PATH + "logdata/partner_account/";
	private static String LOG_FILE_PATH_SUBBIX = ".log";
	private static Map<String, PartnerLogger> map = new HashMap<String, PartnerLogger>();
	private String partnerId = "";
	private PartnerLogger(String partnerId) {
		super();
		this.partnerId = partnerId;
	}
	
	/**
	 * 根据传进来的名称生成动态文件名的日志文件。
	 * @param name
	 * @return 日志类
	 */
	public static PartnerLogger getDynamicLogger(String name) {  
		synchronized (map) {
			if (map.containsKey(name)){
				return map.get(name);
			}
			PartnerLogger logger = generateLogger(name);
			map.put(name, logger);
			return logger;
		}
	}
	
    private static PartnerLogger generateLogger(String name) {  
    	PartnerLogger lotteryLogger = new PartnerLogger(name); 
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
        appender.setFile(LOG_FILE_PATH_PREFIX + name + LOG_FILE_PATH_SUBBIX);  
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
		return LOG_FILE_PATH_PREFIX + this.partnerId + LOG_FILE_PATH_SUBBIX;
	}
}
