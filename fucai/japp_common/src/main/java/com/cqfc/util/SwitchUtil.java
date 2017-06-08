package com.cqfc.util;

import com.jami.util.Log;

/**
 * @author liwh
 */
public class SwitchUtil {

	public static final String TIMER_SWITCH = System.getProperty("TIMER_SWITCH");

	public static final String MQ_CONSUMER_SWITCH = System.getProperty("MQ_CONSUMER_SWITCH");

	public static final String TEST_ENV_SWITCH = System.getProperty("TEST_ENV_SWITCH");

	public static final String TEST_ACCOUNT_SWITCH = System.getProperty("TEST_ACCOUNT_SWITCH");

	/**
	 * 定时器（0关闭、1开放）
	 * 
	 * @return
	 */
	public static boolean timerIsOpen() {
		if (null == TIMER_SWITCH || "".equals(TIMER_SWITCH)) {
			Log.run.warn("定时器阀值未设置,默认启用定时任务,TIMER_SWITCH=%s", TIMER_SWITCH);
			return true;
		}
		return TIMER_SWITCH.equals("1") ? true : false;
	}

	/**
	 * MQ（0关闭、1开放）
	 * 
	 * @return
	 */
	public static boolean mqConsumerIsOpen() {
		if (null == MQ_CONSUMER_SWITCH || "".equals(MQ_CONSUMER_SWITCH)) {
			Log.run.warn("MQ开关阀值未设置,默认启用MQ,MQ_CONSUMER_SWITCH=%s", MQ_CONSUMER_SWITCH);
			return true;
		}
		return MQ_CONSUMER_SWITCH.equals("1") ? true : false;
	}

	/**
	 * 测试/正式环境开关(输入参数1为正式环境)
	 * 
	 * @return
	 */
	public static boolean isTestEnvOpen() {
		boolean flag = false;

		if (null == TEST_ENV_SWITCH || "".equals(TEST_ENV_SWITCH)) {
			Log.run.warn("出票测试开关阀值不存在,TEST_ENV_SWITCH=%s", TEST_ENV_SWITCH);
			flag = true;
		}
		flag = TEST_ENV_SWITCH.equals("1") ? false : true;
		return flag;
	}

	/**
	 * 测试/正式账号开关(输入参数1为正式账号)
	 * 
	 * @return
	 */
	public static boolean isTestAccountOpen() {
		boolean flag = false;

		if (null == TEST_ACCOUNT_SWITCH || "".equals(TEST_ACCOUNT_SWITCH)) {
			Log.run.warn("出票测试开关阀值不存在,TEST_ACCOUNT_SWITCH=%s", TEST_ACCOUNT_SWITCH);
			flag = true;
		}
		flag = TEST_ACCOUNT_SWITCH.equals("1") ? false : true;
		return flag;
	}

}
