package com.cqfc.access.util;

public interface StatusCodeConst {
	// 投注成功
	public static final String STATUS_CODE_BET_SUCCESS = "0";
	// 投注失败
	public static final String STATUS_CODE_BET_FAILURE = "-1";
	// 参数数据加密不一致
	public static final String STATUS_CODE_SIG_INCONSISTENT = "-2";
	// 参数数据加密不一致
	public static final String STATUS_CODE_PARAM_INCOMPLETE = "-3";
	// 渠道ID不存在
	public static final String STATUS_CODE_CHANNELID_NOEXIST = "-4";
	// 应用程序异常
	public static final String STATUS_CODE_APPLICATION_EXCEPTION = "-11";
	// 参数错误
	public static final String STATUS_CODE_PARAM_ERROR = "-13";
	// 参数格式不正确
	public static final String STATUS_CODE_PARAM_FROMAT_INCORRECT = "-14";
	// 文件没找到
	public static final String STATUS_CODE_FILE_NOTFOUND = "-15";
	// 其它未知错误
	public static final String STATUS_CODE_UNKNOWN_ERROR = "-19";
	// 网站平台交易流水号重复
	public static final String STATUS_CODE_CHANNEL_TICKECTID_REPERT = "-20";
	// 额度不足
	public static final String STATUS_CODE_LIMIT_LACK = "-2001";
	// 投注帐户异常
	public static final String STATUS_CODE_ACCOUNT_EXCEPTION = "-2017";
	// 彩种不支持多期追号
	public static final String STATUS_CODE_NOTSUPPORT_MORE_APPENDTASK = "-3003";
	// 彩种不支持该玩法
	public static final String STATUS_CODE_NOTSUPPORT_PLAYTYPE = "-3004";
	// 商品没找到
	public static final String STATUS_CODE_WARE_NOTFOUND = "-3301";
	// 商品不在销售状态
	public static final String STATUS_CODE_WARE_NOTINSELLSTATE = "-3302";
	// 商品已停售
	public static final String STATUS_CODE_WARE_STOP_SELL = "-3303";
}
