package com.cqfc.createsql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author liwh
 *
 */
public class CreateBussinessOrderSql {

	public static void main(String[] args) throws Exception {
		File f = new File("D:" + File.separator + "20140813init_business_order.sql");
		// 用FileOutputSteam包装文件，并设置文件可追加
		OutputStream out = new FileOutputStream(f, true);
		// 字符数组
		StringBuffer strBuf = new StringBuffer();
		for (int j = 0; j < 10; j++) {
			String dbIndex = String.valueOf(j);
			if (dbIndex.length() < 2) {
				dbIndex = "0" + dbIndex;
			}
			for (int i = 0; i < 100; i++) {
				String index = String.valueOf(i);
				if (index.length() < 2) {
					index = "0" + index;
				}
				strBuf.append("DROP TABLE IF EXISTS fucai_order_" + dbIndex + ".t_lottery_order_" + index + ";\n");
				strBuf.append("CREATE TABLE fucai_order_" + dbIndex + ".t_lottery_order_" + index + " (\n");
				strBuf.append("		orderId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',\n");
				strBuf.append("		lotteryId varchar(32) NOT NULL COMMENT '彩种ID',\n");
				strBuf.append("		partnerId varchar(64) NOT NULL COMMENT '渠道ID',\n");
				strBuf.append("		userId bigint(20) NOT NULL COMMENT '用户ID',\n");
				strBuf.append("		issueNo varchar(64) NOT NULL COMMENT '期号',\n");
				strBuf.append("		orderNo varchar(64) NOT NULL COMMENT '订单编号',\n");
				strBuf.append("		orderType tinyint(4) NOT NULL COMMENT '订单类型：1直投订单 2追号订单',\n");
				strBuf.append("		orderStatus tinyint(4) NOT NULL COMMENT '订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功 11订单取消',\n");
				strBuf.append("		totalAmount bigint(20) NOT NULL COMMENT '投注总金额',\n");
				strBuf.append("		orderContent varchar(1024) NOT NULL COMMENT '投注内容',\n");
				strBuf.append("		multiple int(11) NOT NULL COMMENT '倍数',\n");
				strBuf.append("		playType varchar(32) NOT NULL COMMENT '玩法',\n");
				strBuf.append("		paySerialNumber varchar(64) DEFAULT NULL COMMENT '支付流水号',\n");
				strBuf.append("		isSyncSuccess tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否同步到全局数据库 0为同步  1同步成功 2同步失败',\n");
				strBuf.append("		tradeId varchar(64) NOT NULL DEFAULT '0' COMMENT '合作商交易ID',\n");
				strBuf.append("		realName varchar(32) DEFAULT NULL COMMENT '真实姓名',\n");
				strBuf.append("		cardNo varchar(64) DEFAULT NULL COMMENT '身份证号',\n");
				strBuf.append("		mobile varchar(32) DEFAULT NULL COMMENT '手机号',\n");
				strBuf.append("		createTime datetime NOT NULL COMMENT '创建时间',\n");
				strBuf.append("		lastUpdateTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',\n");
				strBuf.append("		PRIMARY KEY (orderId),\n");
				strBuf.append("		UNIQUE KEY orderNo (orderNo)\n");
				strBuf.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';\n\n");
			}
		}
		out.write(strBuf.toString().getBytes()); // 向文件中写入数据
		out.write('\r'); // \r\n表示换行
		out.write('\n');
		out.close(); // 关闭输出流
		System.out.println("写入成功！");
	}

}
