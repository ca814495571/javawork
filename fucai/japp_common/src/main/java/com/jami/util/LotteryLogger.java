package com.jami.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cqfc.util.Pair;
/**
 * 日志类
 * @author HowKeyond
 *
 */
public class LotteryLogger {
	
	private static final String FQCN = LotteryLogger.class.getCanonicalName();
	
	protected Logger logger = null;
	
	protected LotteryLogger(){
		
	}
	
	/**
	 * 通过String类型生成Logger的构造函数，私有，避免外部直接生成Logger对象
	 * @param type Logger参数
	 */
	protected LotteryLogger(String type) {
		logger = Logger.getLogger(type);
	}
	
	/**
	 * 通过Class类型生成Logger的构造函数，私有，避免外部直接生成Logger对象
	 * @param type Logger参数
	 */
	private LotteryLogger(Class type) {
		logger = Logger.getLogger(type);
	}
	
	/**
	 * 根据String生成Logger对象
	 * @param type Logger参数
	 * @return 返回LotteryLogger
	 */
	public static LotteryLogger getLogger(String type) {
		return new LotteryLogger(type);
	}

	/**
	 * 根据Class生成Logger对象
	 * @param type Logger参数
	 * @return 返回LotteryLogger
	 */
	public static LotteryLogger getLogger(Class type) {
		return new LotteryLogger(type);
	}
	
	/**
	 * 记debug 日志
	 * @param message 日志信息
	 * @param t 异常信息
	 */
	public void debug(Object message, Throwable t){
		logger.log(FQCN, Level.DEBUG, message, t);
	}
	
	private Pair<String,Throwable> dealMsg(String msg, Object... args){
		Pair<String,Throwable> result = new Pair<String,Throwable>();
		if (args != null && args.length > 0) {
			Object e = args[args.length - 1];
			if (e instanceof Throwable) {
				args[args.length - 1] = "";
				result.second((Throwable) e);
			}
			String format = String.format(msg, args);
			result.first(format);
		} else {
			result.first(msg);
		}
		return result;
	}
	/**
	 * 
	 * 记debug 日志
	 * @param message 日志信息
	 * @param args 日志信息参数
	 */
	public void debug(Object message, Object... args){
		if (!logger.isDebugEnabled()) return;
		if(message instanceof String){
			Pair<String, Throwable> msg = dealMsg((String)message, args);
			logger.log(FQCN, Level.DEBUG, msg.first(), msg.second());
		}else{
			logger.log(FQCN, Level.DEBUG, message, null);
		}
	}
	
	/**
	 * 记info 日志
	 * @param message 日志信息
	 * @param t 异常信息
	 */
	public void info(Object message, Throwable t){
		logger.log(FQCN, Level.INFO, message, t);
	}

	/**
	 * 
	 * 记info 日志
	 * @param message 日志信息
	 * @param args 日志信息参数
	 */
	public void info(Object message, Object... args){
		if (!logger.isInfoEnabled()) return;
		if(message instanceof String){
			Pair<String, Throwable> msg = dealMsg((String)message, args);
			logger.log(FQCN, Level.INFO, msg.first(), msg.second());
		}else{
			logger.log(FQCN, Level.INFO, message, null);
		}
	}
	
	/**
	 * 记warn 日志
	 * @param message 日志信息
	 * @param t 异常信息
	 */
	public void warn(Object message, Throwable t){
		logger.log(FQCN, Level.WARN, message, t);
	}

	/**
	 * 
	 * 记warn 日志
	 * @param message 日志信息
	 * @param args 日志信息参数
	 */
	public void warn(Object message, Object... args){
		if (!logger.isEnabledFor(Level.WARN)) return;
		if(message instanceof String){
			Pair<String, Throwable> msg = dealMsg((String)message, args);
			logger.log(FQCN, Level.WARN, msg.first(), msg.second());
		}else{
			logger.log(FQCN, Level.WARN, message, null);
		}
	}
	
	/**
	 * 记error 日志
	 * @param message 日志信息
	 * @param t 异常信息
	 */
	public void error(Object message, Throwable t){
		logger.log(FQCN, Level.ERROR, message, t);
	}

	/**
	 * 
	 * 记error 日志
	 * @param message 日志信息
	 * @param args 日志信息参数
	 */
	public void error(Object message, Object... args){
		if (!logger.isEnabledFor(Level.ERROR)) return;
		if(message instanceof String){
			Pair<String, Throwable> msg = dealMsg((String)message, args);
			logger.log(FQCN, Level.ERROR, msg.first(), msg.second());
		}else{
			logger.log(FQCN, Level.ERROR, message, null);
		}
	}
	
	/**
	 * 记fatal 日志
	 * @param message 日志信息
	 * @param t 异常信息
	 */
	public void fatal(Object message, Throwable t){
		logger.log(FQCN, Level.FATAL, message, t);
	}

	/**
	 * 
	 * 记fatal 日志
	 * @param message 日志信息
	 * @param args 日志信息参数
	 */
	public void fatal(Object message, Object... args){
		if(message instanceof String){
			Pair<String, Throwable> msg = dealMsg((String)message, args);
			logger.log(FQCN, Level.FATAL, msg.first(), msg.second());
		}else{
			logger.log(FQCN, Level.FATAL, message, null);
		}
	}
}
