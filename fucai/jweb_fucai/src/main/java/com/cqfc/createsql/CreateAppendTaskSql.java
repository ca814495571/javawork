package com.cqfc.createsql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CreateAppendTaskSql {

	public static void main(String[] args) throws Exception {
		File f = new File("D:" + File.separator + "20140813init_append_task.sql");
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
				strBuf.append("CREATE TABLE fucai_append_" + dbIndex + ".t_lottery_append_task_" + index + " (\n");
				strBuf.append("		appendTaskId varchar(128) NOT NULL COMMENT '追号任务ID',\n");
				strBuf.append("		partnerId varchar(64) NOT NULL COMMENT '渠道ID',\n");
				strBuf.append("		lotteryId varchar(32) NOT NULL COMMENT '彩种ID',\n");
				strBuf.append("		ball varchar(1024) NOT NULL COMMENT '投注内容',\n");
				strBuf.append("		beginIssueNo varchar(64) NOT NULL COMMENT '开始期号',\n");
				strBuf.append("		appendQuantity int(11) NOT NULL COMMENT '追号期数',\n");
				strBuf.append("		remainingQuantity int(11) NOT NULL COMMENT '剩余期数',\n");
				strBuf.append("		finishedNum int(11) NOT NULL DEFAULT '0' COMMENT '完成期数',\n");
				strBuf.append("		cancelNum int(11) NOT NULL DEFAULT '0' COMMENT '取消期数',\n");
				strBuf.append("		stopFlag int(11) NOT NULL COMMENT '终止条件：0不停追  1中任意奖停追  2中大奖停追',\n");
				strBuf.append("		appendStatus int(11) NOT NULL DEFAULT '1' COMMENT '追号任务状态：1追号正常  2追号完成',\n");
				strBuf.append("		appendTotalMoney bigint(20) NOT NULL COMMENT '追号总金额',\n");
				strBuf.append("		finishedMoney bigint(20) NOT NULL DEFAULT '0' COMMENT '完成金额',\n");
				strBuf.append("		cancelMoney bigint(20) NOT NULL DEFAULT '0' COMMENT '取消金额',\n");
				strBuf.append("		winningTotalMoney bigint(20) NOT NULL DEFAULT '0' COMMENT '总中奖金额',\n");
				strBuf.append("		perNoteNumber int(11) NOT NULL COMMENT '每次注数',\n");
				strBuf.append("		userId bigint(20) NOT NULL COMMENT '用户ID',\n");
				strBuf.append("		playType varchar(32) NOT NULL COMMENT '玩法',\n");
				strBuf.append("		newAppendIssueNo varchar(64) DEFAULT NULL COMMENT '最新追过的期号',\n");
				strBuf.append("		freezeSerialNumber varchar(64) NOT NULL COMMENT '冻结账户金额流水号',\n");
				strBuf.append("		createTime datetime NOT NULL COMMENT '发起时间',\n");
				strBuf.append("		lastUpdateTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',\n");
				strBuf.append("		PRIMARY KEY (appendTaskId)\n");
				strBuf.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='追号任务表';\n\n");

				strBuf.append("CREATE TABLE fucai_append_" + dbIndex + ".t_lottery_append_task_detail_" + index
						+ " (\n");
				strBuf.append("		detailId bigint(20) NOT NULL AUTO_INCREMENT COMMENT '追号详情ID',\n");
				strBuf.append("		appendTaskId varchar(128) NOT NULL COMMENT '追号任务ID',\n");
				strBuf.append("		partnerId varchar(64) NOT NULL COMMENT '渠道ID',\n");
				strBuf.append("		issueNo varchar(64) NOT NULL COMMENT '期号',\n");
				strBuf.append("		lotteryId varchar(32) NOT NULL COMMENT '彩种ID',\n");
				strBuf.append("		userId bigint(20) NOT NULL COMMENT '用户ID',\n");
				strBuf.append("		playType varchar(64) NOT NULL COMMENT '玩法',\n");
				strBuf.append("		totalMoney bigint(20) NOT NULL COMMENT '投注总金额',\n");
				strBuf.append("		multiple int(11) NOT NULL COMMENT '倍数',\n");
				strBuf.append("		noteNumber int(11) NOT NULL COMMENT '注数',\n");
				strBuf.append("		lotteryNumber varchar(128) DEFAULT NULL COMMENT '当期开奖号码',\n");
				strBuf.append("		afterTaxMoney bigint(20) NOT NULL DEFAULT '0' COMMENT '税后奖金',\n");
				strBuf.append("		appendDetailStatus int(11) NOT NULL DEFAULT '0' COMMENT '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',\n");
				strBuf.append("		createOrderTimes int(11) NOT NULL DEFAULT '0' COMMENT '下单重复次数',\n");
				strBuf.append("		orderNo varchar(128) DEFAULT NULL COMMENT '订单编号',\n");
				strBuf.append("		ball varchar(1024) NOT NULL COMMENT '投注内容',\n");
				strBuf.append("		createTime datetime NOT NULL COMMENT '生成时间',\n");
				strBuf.append("		lastUpdateTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',\n");
				strBuf.append("		PRIMARY KEY (detailId)\n");
				strBuf.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='追号详情表';\n\n");

				strBuf.append("CREATE TABLE fucai_append_" + dbIndex + ".t_lottery_append_task_index_" + index + " (\n");
				strBuf.append("		partnerId varchar(64) NOT NULL COMMENT '渠道ID',\n");
				strBuf.append("		partnerTradeId varchar(64) NOT NULL COMMENT '合作商交易ID',\n");
				strBuf.append("		appendTaskId varchar(128) NOT NULL COMMENT '追号任务ID',\n");
				strBuf.append("		userId bigint(20) NOT NULL COMMENT '用户ID',\n");
				strBuf.append("		createTime datetime NOT NULL COMMENT '创建时间',\n");
				strBuf.append("		UNIQUE KEY partnerId (partnerId,partnerTradeId)\n");
				strBuf.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='追号任务索引表';\n\n");

			}
		}
		out.write(strBuf.toString().getBytes()); // 向文件中写入数据
		out.write('\r'); // \r\n表示换行
		out.write('\n');
		out.close(); // 关闭输出流
		System.out.println("写入成功！");
	}

}
