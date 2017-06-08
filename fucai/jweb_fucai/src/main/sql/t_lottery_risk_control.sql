/*==============================================================*/
/* Table: t_lottery_ticket_num                                */
/*==============================================================*/
drop table if exists t_lottery_ticket_num;
create table t_lottery_ticket_num 
(
   gameId               varchar(64)                    not null comment '彩种ID',
   issue                varchar(64)                    null  comment '期号',
   setNo                varchar(6)                     null  comment '机器码',
   successNum           bigint                         null default 0 comment '成功数',
   failedNum            bigint                         null default 0 comment '失败数',
   primary key (gameId,issue,setNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '成功及失败订单数';

drop table if exists t_lottery_ticket_statistic;
create table t_lottery_ticket_statistic
(
   gameId                varchar(64)                    not null comment '彩种ID',
   issue                 varchar(64)                    not null comment '期号',
   outTicketSuccessNum   bigint                         null default 0 comment '平台成功出票数',    
   successTicketNum      bigint                         null default 0 comment '平台成功订单数', 
   successTotalMoney     bigint                         null default 0 comment '平台成功销售金额',
   cancelAndSuccessNum   bigint                         null default 0 comment '福彩成功但平台失败的订单数',
   totalWinningMoney     bigint                         null default 0 comment '总中奖金额',
   fucaiTicketNum        bigint                         null default 0 comment '福彩出票成功数',
   fucaiTotalBuy         bigint                         null default 0 comment '福彩交易总金额',
   fucaiTotalWinning     bigint                         null default 0 comment '福彩中奖总金额',
   primary key (gameId,issue)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '按期号统计数据';

drop table if exists t_lottery_ticket_statistic_day;
create table t_lottery_ticket_statistic_day
(
   day                   DATE                           not null default '0000-00-00' comment '日期',
   totalPartnerAccount   bigint                         null default 0 comment '渠道商 帐户总金额',
   totalUserAccount      bigint                         null default 0 comment '用户 帐户总金额',
   totalRecharge         bigint                         null default 0 comment '用户 +渠道商 充值总金额',
   totalWinningMoney     bigint                         null default 0 comment '总中奖金额',
   totalHansel           bigint                         null default 0 comment '派发彩金金额',
   invalidHansel         bigint                         null default 0 comment '过期彩金金额',
   fucaiTotalBuy         bigint                         null default 0 comment '福彩交易总金额',
   totalWithdraw         bigint                         null default 0 comment '用户 +渠道商 提现金额',
   totalTicketNum        bigint                         null default 0 comment '按天统计的订单数',
   paylogNum             bigint                         null default 0 comment '支付流水数据',
    primary key (day)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '按日期统计数据';