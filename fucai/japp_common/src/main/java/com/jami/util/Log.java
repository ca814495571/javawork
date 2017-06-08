package com.jami.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log {

	private static final String FQCN = LotteryLogger.class.getCanonicalName();
	private static Logger errorLogger = Logger.getLogger("error");
	public static LotteryLogger run = LotteryLogger.getLogger("run");
	public static LotteryLogger perf = LotteryLogger.getLogger("perf");
	public static LotteryLogger fucaibiz = LotteryLogger.getLogger("fucaibiz");

	/**
	 * 记error 日志
	 * 
	 * @param message
	 *            日志信息
	 * @param t
	 *            异常信息
	 */
	public static void error(Object message, Throwable t) {
		errorLogger.log(FQCN, Level.ERROR, message, t);
	}

	/**
	 * 
	 * 记error 日志
	 * 
	 * @param message
	 *            日志信息
	 * @param args
	 *            日志信息参数
	 */
	public static void error(Object message, Object... args) {
		if (!errorLogger.isEnabledFor(Level.ERROR))
			return;
		if (message instanceof String) {
			if (args != null && args.length > 0) {
				Object e = args[args.length - 1];
				if (e instanceof Throwable) {
					args[args.length - 1] = "";
					String format = String.format((String) message, args);
					errorLogger.log(FQCN, Level.ERROR, format, (Throwable) e);
					return;
				}
			}
			String format = String.format((String) message, args);
			errorLogger.log(FQCN, Level.ERROR, format, null);
		} else {
			errorLogger.log(FQCN, Level.ERROR, message, null);
		}
	}
}
