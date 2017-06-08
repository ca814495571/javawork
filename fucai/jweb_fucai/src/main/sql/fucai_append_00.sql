
create table fucai_append_00.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_00.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_00.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_00.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_00.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_00.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_01.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_01.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_01.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_01.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_01.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_01.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_02.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_02.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_02.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_02.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_02.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_02.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_03.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_03.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_03.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_03.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_03.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_03.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_04.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_04.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_04.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_04.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_04.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_04.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_05.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_05.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_05.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_05.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_05.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_05.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_06.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_06.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_06.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_06.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_06.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_06.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_07.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_07.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_07.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_07.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_07.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_07.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_08.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_08.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_08.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_08.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_08.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_08.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';





create table fucai_append_09.t_lottery_append_task_detail_00 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_09.t_lottery_append_task_index_00 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_09.t_lottery_append_task_00 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

create table fucai_append_09.t_lottery_append_task_detail_01 
(
   detailId             bigint                         not null auto_increment comment '追号详情ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   issueNo              varchar(64)                    not null comment '期号',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(64)                    not null comment '玩法',
   totalMoney           bigint                         not null comment '投注总金额',
   multiple             int                            not null comment '倍数',
   noteNumber           int                            not null comment '注数',
   lotteryNumber        varchar(128)                   null comment '当期开奖号码',
   afterTaxMoney        bigint                         not null default 0 comment '税后奖金',
   appendDetailStatus   int                            not null default 0 comment '追号详情状态：0等待交易  1交易中  2交易成功  3交易失败',
   createOrderTimes     int                            not null default 0 comment '下单重复次数',
   orderNo              varchar(128)                   null comment '订单编号',
   ball                 varchar(1024)                  not null comment '投注内容',
   createTime           datetime                       not null comment '生成时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (detailId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号详情表';
create table fucai_append_09.t_lottery_append_task_index_01 
(
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerTradeId       varchar(64)                    not null comment '合作商交易ID',
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   userId               bigint                         not null comment '用户ID',
   createTime           datetime                       not null comment '创建时间',
   unique(partnerId,partnerTradeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务索引表';
create table fucai_append_09.t_lottery_append_task_01 
(
   appendTaskId         varchar(128)                   not null comment '追号任务ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   lotteryId            varchar(32)                    not null comment '彩种ID',
   ball                 varchar(1024)                  not null comment '投注内容',
   beginIssueNo         varchar(64)                    not null comment '开始期号',
   appendQuantity       int                            not null comment '追号期数',
   remainingQuantity    int                            not null comment '剩余期数',
   finishedNum    		int                            not null default 0 comment '完成期数',
   cancelNum    		int                            not null default 0 comment '取消期数',
   stopFlag             int                            not null comment '终止条件：0不停追  1中任意奖停追  2中大奖停追',
   appendStatus         int                            not null default 1 comment '追号任务状态：1追号正常  2追号完成',
   appendTotalMoney     bigint                         not null comment '追号总金额',
   finishedMoney     	bigint                         not null default 0 comment '完成金额',
   cancelMoney     		bigint                         not null default 0 comment '取消金额',
   winningTotalMoney    bigint                         not null default 0 comment '总中奖金额',
   perNoteNumber        int                            not null comment '每次注数',
   userId               bigint                         not null comment '用户ID',
   playType             varchar(32)                    not null comment '玩法',
   newAppendIssueNo     varchar(64)                    null comment '最新追过的期号',
   freezeSerialNumber   varchar(64)                    not null comment '冻结账户金额流水号',
   createTime           datetime                       not null comment '发起时间',
   lastUpdateTime       timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (appendTaskId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '追号任务表';

