/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 11                       */
/* Created on:     2014/6/17 15:42:22                           */
/*==============================================================*/

/*==============================================================*/
/* Table: t_lottery_partner_account                             */
/*==============================================================*/
create table t_lottery_partner_account 
(
   partnerId            varchar(64)                    not null	comment '渠道id',
   totalAmount          bigint                         not null default 0	comment '总金额',
   freezeAmount         bigint                         not null default 0	comment '冻结金额',
   usableAmount         bigint                         not null default 0	comment '可用金额',
   creditLimit          bigint                         not null default 0	comment '信用额度',
   alarmValue           bigint                         not null default 0	comment '告警值',
   state                smallint                       not null default 1	comment '状态：1正常 2冻结',
   createTime           datetime                       not null	comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (partnerId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道账户表';

/*==============================================================*/
/* Table: t_lottery_partner_account_log                         */
/*==============================================================*/
create table t_lottery_partner_account_log 
(
   logId                bigint                         not null auto_increment comment '流水ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   state                smallint                       not null default 0 comment '收支状态：1充值  2支付  4退款  5派奖',
   totalAmount          bigint                         not null default 0 comment '变更前总金额',
   accountAmount        bigint                         not null default 0 comment '变更金额,发生捐款则为负数',
   remainAmount         bigint                         not null default 0 comment '变更后金额',
   serialNumber         varchar(64)                    not null unique comment '流水号',
   ext               	varchar(256)                   null comment '扩展信息',
   remark               varchar(256)                   null comment '备注',
   createTime           datetime                       not null comment '创建时间',
   primary key (logId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道账户流水表';

/*==============================================================*/
/* Table: t_lottery_partner_recharge                   */
/*==============================================================*/
create table t_lottery_partner_recharge
(
   rechargeId           bigint                         not null auto_increment comment '充值ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   serialNumber         varchar(64)                    null comment '流水号',
   rechargeAmount       bigint                         not null default 0 comment '充值金额',
   rechargeType         varchar(64)                    null comment '充值方式',
   remark               varchar(256)                   null comment '备注',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (rechargeId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道账户充值记录表';

/*==============================================================*/
/* Table: t_lottery_partner_preapply                   */
/*==============================================================*/
create table t_lottery_partner_preapply
(
   preApplyId           bigint                         not null auto_increment comment '预申请ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerUniqueNo   	varchar(64)                    not null comment '渠道订单唯一号',
   preMoney       		bigint                         not null comment '预存款金额',
   status         		smallint                       not null default 0 comment '申请状态:0 待审核 1 审核通过 2 审核未通过',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (preApplyId),
   unique key partnerUniqueNo(partnerId, partnerUniqueNo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道预申请表';


