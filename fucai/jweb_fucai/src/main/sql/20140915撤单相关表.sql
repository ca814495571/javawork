/* 撤单信息表 */
create table t_lottery_cancel_order (
   	orderNo varchar(64) NOT NULL COMMENT '订单编号',
   	partnerId varchar(64) NOT NULL COMMENT '渠道ID',
   	lotteryId varchar(32) NOT NULL COMMENT '彩种ID',
   	issueNo varchar(64) NOT NULL COMMENT '期号',
   	outTicketStatus tinyint(4) DEFAULT '3' COMMENT '订单出票状态：3出票中 4已出票待开奖 5出票失败',
	totalAmount bigint(20) NOT NULL COMMENT '投注总金额',
	orderContent varchar(1024) NOT NULL COMMENT '投注内容',
	userId bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  	multiple int(11) NOT NULL DEFAULT '1' COMMENT '倍数',
  	playType varchar(32) NOT NULL DEFAULT '' COMMENT '玩法',
	createTime datetime NOT NULL COMMENT '创建时间',
	lastUpdateTime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	PRIMARY KEY (orderNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='撤单信息表';
