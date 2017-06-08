package com.cqfc.util;


public interface ConstantsUtil {

	// 每次请求最多包含50个订单
	public static final int MAX_TICKET_PER_REQUEST = 50;

	// 最大投注金额为不超过10万元，单位是分
	public static final long MAX_TOTAL_MONEY = 10000000;

	// 定义device type
	public static final String DEVICE_TYPE_BUSINESS_CONTROLLER = "businessController";
	public static final int DEVICE_TYPE_TICKET_BUSINESS = 10;
	public static final int DEVICE_TYPE_QUERY_TICKET_BUSINESS = 17;
	public static final int DEVICE_TYPE_OTHER_BUSINESS = 11;

	// 表示路由时不作限制，任一个都可以
	public static final int ANY = -1;

	// businessController的最大个数
	public static int MAX_BUSINESSCONTROLLER_NUM = 100;

	// 模块名称定义
	public static final String MODULENAME_BUSINESS_CONTROLLER = "businessController";
	public static final String MODULENAME_LOTTERY_ISSUE = "lotteryIssue";
	public static final String MODULENAME_USER_ACCOUNT = "userAccount";
	public static final String MODULENAME_PARTNER_ACCOUNT = "partnerAccount";
	public static final String MODULENAME_PARTNER = "partner";
	public static final String MODULENAME_TICKET_ISSUE = "ticketIssue";
	public static final String MODULENAME_PARTNER_ORDER = "partnerOrder";
	public static final String MODULENAME_USER_ORDER = "userOrder";
	public static final String MODULENAME_APPEND_TASK = "appendTask";
	public static final String MODULENAME_TICKET_WINNING = "ticketWinning";
	public static final String MODULENAME_ACCESS_BACK = "accessBack";
	public static final String MODULENAME_SOLR = "solr";
	public static final String MODULENAME_CANCEL_ORDER = "cancelOrder";
	public static final String MODULENAME_RISK_CONTROL = "riskControl";

	public static final String METHODNAME_PROCESSMESSAGE = "ProcessMessage";
	public static final String METHODNAME_ISTICKETSUCCESS = "isTicketSuccess";
	public static final String METHODNAME_CREATEAPPENDORDER = "createAppendOrder";
	public static final String METHODNAME_CREATEORDERPROCESS = "createOrderProcess";
	public static final String METHODNAME_FINDORDERPROCESS = "findOrderProcess";
	public static final String METHODNAME_BATCHFINDORDERPROCESS = "batchFindOrderProcess";

	public static final String CORENAME_USERINFO = "userinfo";
	public static final String CORENAME_HANDSEL = "handsel";
	public static final String CORENAME_RECHARGE = "recharge";

	// ---定义状态码(statusCode,status,errorCode)---
	// 系统错误
	public static final String STATUS_CODE_SYSTEM_ERROR = "9999";
	// 等待交易
	public static final String STATUS_CODE_WAIT_TRADE = "0000";
	// 交易中
	public static final String STATUS_CODE_TRADING = "0001";
	// 交易成功
	public static final String STATUS_CODE_TRADE_SUCCESS = "0002";
	// 交易失败
	public static final String STATUS_CODE_RRADE_FAIL = "0003";
	// 发起成功
	public static final String STATUS_CODE_INITIATE_SUCCESS = "0004";
	// 合买满员
	public static final String STATUS_CODE_CHIPPED_FULL = "0005";
	// 合买参与成功
	public static final String STATUS_CODE_CHIPPEDIN_SUCCESS = "0006";
	// XML格式化错误
	public static final String STATUS_CODE_SCHEMA_VALIDATE_INCORRECT = "0007";
	// 合作商id不存在或者参数与xml文件中的不一致
	public static final String STATUS_CODE_PARTNERID_OR_XML_ERROR = "0008";
	// 系统错误，即由未捕获的异常导致的错误
	public static final String STATUS_CODE_SYSTEM_MISTAKE = "0009";
	// 成功
	public static final String STATUS_CODE_SUCCESS = "0010";
	// 未满员
	public static final String STATUS_CODE_NOT_FULL = "0011";
	// 部分成功
	public static final String STATUS_CODE_PART_SUCCESS = "0012";
	// 用户不存在
	public static final String STATUS_CODE_USER_NOTEXIST = "9000";
	// 未查询到数据
	public static final String STATUS_CODE_NOTQUERY_DATA = "9100";
	// 金额不足
	public static final String STATUS_CODE_AMOUNT_LESS = "9001";
	// 系统错误
	public static final String STATUS_CODE_SYSTEM_FAIL = "9002";
	// 操作数据库失败
	public static final String STATUS_CODE_DB_ERROR = "9003";
	// 期次错误(重复)
	public static final String STATUS_CODE_ISSUE_ERROR = "9004";
	// 追号订单不存在
	public static final String STATUS_CODE_CHASEORDER_NOTEXIST = "9005";
	// 参数为空
	public static final String STATUS_CODE_PARAM_NULL = "9006";
	// 销售期已过 停止销售
	public static final String STATUS_CODE_STOP_SAIL = "9008";
	// 重复订单
	public static final String STATUS_CODE_REPEAT_ORDER = "9009";
	// 超过最大注数
	public static final String STATUS_CODE_OVER_LARGESTNUM = "9010";
	// 票总金额错误
	public static final String STATUS_CODE_TOTALAMOUNT_ERROR = "9011";
	// 不存在的期次
	public static final String STATUS_CODE_NOTEXIST_ISSUENO = "9012";
	// 用户id错误
	public static final String STATUS_CODE_USERID_ERROR = "9013";
	// 期次错误(重复)
	public static final String STATUS_CODE_ISSUENO_ERROR = "9014";
	// 订单不存在
	public static final String STATUS_CODE_ORDER_NOTEXIST = "9015";
	// 投注号码格式错误
	public static final String STATUS_CODE_NUMBERFORMAT_ERROR = "9016";
	// 投注金额错误
	public static final String STATUS_CODE_BETTINGAMOUNT_ERROR = "9017";
	// 超过最大允许倍数
	public static final String STATUS_CODE_OVER_MAXMULTIPLE = "9018";
	// 期次数错误
	public static final String STATUS_CODE_ISSUENUM_ERROR = "9019";
	// 订单总数与实际不匹配
	public static final String STATUS_CODE_TOTALORDER_NOTACTUAL = "9020";
	// 停追类型错误
	public static final String STATUS_CODE_STOPCHASE_ERROR = "9021";
	// 当前期不能取消追号
	public static final String STATUS_CODE_CANNOT_CANCEL = "9022";
	// 不支持的接入商类型
	public static final String STATUS_CODE_NOTSUPPORT_PORTAL = "9023";
	// 充值订单号重复
	public static final String STATUS_CODE_RECHARGE_ORDERNOREPEAT = "9024";
	// 不存在获奖信息
	public static final String STATUS_CODE_NO_WINNINGMSG = "9050";
	// 不存在的合作商
	public static final String STATUS_CODE_PARTNER_NOTEXIST = "9051";
	// 加密信息是不一致的
	public static final String STATUS_CODE_SIG_INCONSISTENT = "9052";
	// 不一致的票数与钱数
	public static final String STATUS_CODE_TICKETANDAMOUNT_INCONSISTENT = "9053";
	// 重复的用户
	public static final String STATUS_CODE_REPEAT_USER = "9054";
	// 不存在的充值定单
	public static final String STATUS_CODE_RECHARGEORDER_NOTEXIST = "9055";
	// 扣款异常或者帐号钱数不足
	public static final String STATUS_CODE_DEDUCTMONEY_ERROR = "9056";
	// 非法数字
	public static final String STATUS_CODE_ILLEGAL_DIGITAL = "9057";
	// 权限不足
	public static final String STATUS_CODE_NOT_PERMISSION = "9058";
	// 状态不可用
	public static final String STATUS_CODE_STATUS_CANNOTUSED = "9059";
	// 数据已存在
	public static final String STATUS_CODE_DATA_ISEXIST = "9060";
	// 不存在的文件
	public static final String STATUS_CODE_DOCUMENT_NOTEXIST = "9061";
	// 退款异常(新增)
	public static final String STATUS_CODE_REFUNDMONEY_ERROR = "9062";

	// 定义调用japp_common返回状态码（只供内部使用约定）
	// 返回成功
	public static final String STATUS_CODE_RETURN_SUCCESS = "10000";
	// 类找不到
	public static final String STATUS_CODE_RETURN_CLASS_NOTEXIST = "10001";
	// 方法找不到
	public static final String STATUS_CODE_RETURN_METHOD_NOTEXIST = "10002";
	// 参数不正确
	public static final String STATUS_CODE_RETURN_ARGUMENT_ERROR = "10003";
	// 方法调用错误
	public static final String STATUS_CODE_RETURN_METHOD_INVOKE_ERROR = "10005";
	// Socket连接错误
	public static final String STATUS_CODE_RETURN_CONNECTION_ERROR = "10004";

	// 本地测试公钥名称
	public static final String PUBLIC_SCRECT_NAME = "19_public";
	// 本地测试秘钥名称
	public static final String PRIVATE_SCRECT_NAME = "19_private";	
	// 本地测试密钥别称
	public final static String ALIAS = "cqfc";
	// 本地测试密钥密码
	public final static String KEY_STORE_PASS = "123456";

	// 福彩测试公钥名称
	public static final String FUCAI_PUBLIC_SCRECT_NAME = "42_public";
	// 福彩测试秘钥名称
	public static final String FUCAI_PRIVATE_SCRECT_NAME = "42_private";

	// 日志文件目录
	public static final String LOG_PATH = System.getProperty("log_path") == null ? "" : System.getProperty("log_path")
			+ "/";

	//数据库主库前缀
	public static final String MASTER = "master_";
	
	//数据库从库前缀
	public static final String SLAVE = "slave_";
	
	//分隔符,用于截取msg中body之前内容
	public static final String SEPARATOR_MSG_BODY = "<body>";
	
	
	//测试合作商账号
	public static final String TEST_PARTNERID = "123456789";
	
	//测试合作商账号
	public static final String ERROR_TRANSCODE = "000";
	
	//重庆福彩中心正式地址
	public static final String CHONG_QIN_CENTER_SERVER_URL = "http://119.84.60.81:8080/greatwallweb/main";
}
