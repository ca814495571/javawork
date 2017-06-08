package com.cqfc.util;

public class TicketIssueConstant {	
	/**
	 * 重庆测试服务器接入地址
	 */
	public final static String CHONGQING_FUCAI_SERVER_URL = "http://222.177.23.40:1003/greatwallweb/main";
	public final static String CHONGQING_SERVER_URL = "http://222.177.23.40:1003/greatwallweb/main";
	/**
	 * 重庆测试签名代理名 （alias)
	 */
	public final static String CHONGQING_ALIALS = "leyicai";
	/**
	 * 重庆测试签名密码 （keystorepass)
	 */
	public final static String CHONGQING_KEY_STORE_PASS= "123456";
	/**
	 * 重庆测试渠道商id
	 */
	public final static String CHONGQING_PARTNERID= "42";	
	/**
	 * 重庆测试用户信息
	 */	
	public final static String CHONGQING_USERID = "4200000001";
	/**
	 * 重庆测试公钥名称
	 */
	public static final String CHONGQING_PUBLIC_SCRECT_NAME = "42_public";
	/**
	 *  重庆测试秘钥名称
	 */
	public static final String CHONGQING_PRIVATE_SCRECT_NAME = "42_private";
	/**
	 * 本地签名代理名 （alias)
	 */
	public final static String BENDI_ALIALS = "cqfc";
	/**
	 * 本地签名密码 （keystorepass)
	 */
	public final static String BENDI_KEY_STORE_PASS = "123456";
	/**
	 * 本地接入渠道的id
	 */
	public final static String BENDI_PARTNER_ID = "00860001";
	/**
	 * 本地测试公钥名称
	 */
	public static final String BENDI_PUBLIC_SCRECT_NAME = "19_public";
	/**
	 * 本地测试私钥名称
	 */
	public static final String BENDI_PRIVATE_SCRECT_NAME = "19_private";
	/**
	 * 本地测试公钥名称
	 */
	public static final String PUBLIC_SCRECT_NAME = "19_public";
	/**
	 *  本地测试秘钥名称
	 */
	public static final String PRIVATE_SCRECT_NAME = "tencentcerts";
	/**
	 * 协议版本
	 */
	public final static String VERSION = "1.0";
	/**
	 * 省份
	 */
	public final static String PROVINCE = "cq";
	/**
	 * 000交易码
	 */
	public final static String TRANSCODE000 = "000";
	/**
	 * 102交易码
	 */
	public final static String TRANSCODE102 = "102";
	/**
	 * 104交易码
	 */
	public final static String TRANSCODE104 = "104";
	/**
	 * 103交易码
	 */
	public final static String TRANSCODE103 = "103";	
	/**
	 * 105交易码
	 */
	public final static String TRANSCODE105 = "105";
	/**
	 * 140交易码
	 */
	public final static String TRANSCODE140 = "140";
	/**
	 * 141交易码
	 */
	public final static String TRANSCODE141 = "141";
	/**
	 * 148交易码
	 */
	public final static String TRANSCODE148 = "148";
	/**
	 * 605交易码
	 */
	public final static String TRANSCODE605 = "605";
	/**
	 * 702交易码
	 */
	public final static String TRANSCODE702 = "702";
	/**
	 * 704交易码
	 */
	public static final String TRANSCODE704 = "704";
	/**
	 * 请求返回结果处理字符
	 */
	public final static String RESPONSE_PROCESS_STR = "</msg>";
	public final static int RESPONSE_PROCESS_STR_LENGTH = RESPONSE_PROCESS_STR.length();
	public final static String UNUSE_END = "	error";
	
	/**
	 * 出票结果 1 失败 2成功3 出票中4 订单不存在
	 */
	public final static int SEND_TICKET_RESULT_FAIL = 1;
	public final static int SEND_TICKET_RESULT_SUCCESS = 2;
	public final static int SEND_TICKET_RESULT_PROCESSING = 3;
	public final static int SEND_TICKET_RESULT_NOEXIST_ORDER = 4;
	
	/**
	 * 出票同步返回状态 1 成功 0 失败
	 */
	public final static int STATUS_SEND_TICKET_OK = 1;
	public final static int STATUS_SEND_TICKET_ERROR = 0;
	
	/**
	 * Http请求连接超时时间(毫秒)
	 */
	public final static int HTTP_CONNECTION_TIME_OUT = 10000;
	public final static int HTTP_SOCKET_TIME_OUT = 10000;
	public final static int HTTP_CONNECTION_REQUEST_TIME_OUT = 10000;
	
	
	
}
