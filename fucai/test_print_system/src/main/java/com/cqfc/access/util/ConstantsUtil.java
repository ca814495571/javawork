package com.cqfc.access.util;

public interface ConstantsUtil {
	//交易码 transcode
	public final static String TRANSCODE101 = "101";
	public final static String TRANSCODE201 = "201";
	public final static String TRANSCODE202 = "202";
	public final static String TRANSCODE203 = "203";
	public final static String TRANSCODE204 = "204";
	public final static String TRANSCODE205 = "205";
	public final static String TRANSCODE206 = "206";
	public final static String TRANSCODE301 = "301";
	public final static String TRANSCODE302 = "302";
	public final static String TRANSCODE306 = "306";
	public final static String TRANSCODE307 = "307";
	
	// 分隔符
	// 逗号 ,
	public final static String SEPARATOR_DOUHAO = ",";
	// 冒号 :
	public final static String SEPARATOR_MAOHAO = ":";
	// 破折号 -
	public final static String SEPARATOR_POZHEHAO = "-";
	// 下划线 _
	public final static String SEPARATOR_XIAHUAXIAN = "_";
	// 井号 #
	public final static String SEPARATOR_JINGHAO = "#";
	// 竖线 |
	public final static String SEPARATOR_SHUXIAN = "|";
	// 斜线 /
	public final static String SEPARATOR_XIEXIAN = "/";
	// 竖线转义 |
	public final static String SEPARATOR_SHUXIAN_REGX = "\\|";
	
	
	/**
	 * Http请求连接超时时间(毫秒)
	 */
	public final static int HTTP_CONNECTION_TIME_OUT = 10000;
	public final static int HTTP_SOCKET_TIME_OUT = 10000;
	public final static int HTTP_CONNECTION_REQUEST_TIME_OUT = 10000;
}
