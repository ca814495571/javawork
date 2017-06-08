/* 中奖结果表 */
create table t_lottery_winning_result (
   id				 int not null auto_increment comment 'id',
   partnerId         varchar(64) not null comment '渠道ID',
   userId            bigint comment '用户ID',
   orderNo           varchar(64) not null unique comment '订单编号',
   orderType         tinyint(4) not null comment '订单类型：1直投订单 2追号订单',
   dbName            varchar(64) not null comment '数据库名称',
   tableName         varchar(64) not null comment '数据库表名称',
   lotteryId         varchar(32) not null comment '彩种Id',
   issueNo           varchar(64) not null comment '期号',
   playType          varchar(32) not null comment '玩法',
   multiple          int not null comment '倍数',
   orderContent      varchar(1024) not null comment '投注内容',
   winningAmount     bigint not null comment '中奖总金额',
   createTime        datetime comment '创建时间',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='中奖结果表';