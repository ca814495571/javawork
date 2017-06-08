/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 11                       */
/* Created on:     2014/6/17 16:05:02                           */
/*==============================================================*/


/*==============================================================*/
/* Table: t_lottery_partner_user                                */
/*==============================================================*/
drop table if exists t_lottery_partner_user;
create table t_lottery_partner_user 
(
   id                   bigint                         not null auto_increment comment 'ID',
   userId               bigint                         not null comment '用户ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   partnerSecond        varchar(64)                    null comment '二级渠道',
   partnerThird         varchar(64)                    null comment '三级渠道',
   partnerFourth        varchar(64)                    null comment '四级渠道',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道商与用户关联关系表';


/*==============================================================*/
/* Table: t_lottery_withdraw_apply                              */
/*==============================================================*/
drop table if exists t_lottery_withdraw_apply;
create table t_lottery_withdraw_apply 
(
   applyId              bigint                         not null auto_increment comment '提现申请ID',
   userId               bigint                         not null comment '用户ID',
   realName             varchar(64)                    null comment '用户姓名',
   partnerId            varchar(64)                    not null comment '渠道ID',
   serialNumber         varchar(64)                    not null comment '流水号',
   withdrawAccountId    bigint                    	   null comment '提款帐号ID',
   withdrawAmount       bigint                         null comment '提款金额',
   totalAmount          bigint                         null comment '提款前总金额',
   auditState           smallint                       not null default 1 comment '审核状态: 1未审核 2审核通过 3审核不通过',
   auditRemark          varchar(256)                   null comment '审核备注',
   auditTime            datetime                       null comment '审核时间',
   auditId              bigint                         null comment '审核人ID',
   auditName            varchar(64)                    null comment '审核人昵称',
   partnerApplyId       varchar(64)                    not null comment '提现请求合作商的id',
   withdrawMsgId        varchar(64)                    null DEFAULT '0' comment '提现任务信息id',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (applyId),
   unique key partnerApplyId(partnerId, partnerApplyId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '提现申请表';

create index t_lottery_withdraw_apply_userId_index on t_lottery_withdraw_apply(userId);
create index t_lottery_withdraw_apply_sn_index on t_lottery_withdraw_apply(serialNumber);

/*==============================================================*/
/* Table: t_lottery_user_preapply                              */
/*==============================================================*/
drop table if exists t_lottery_user_preapply;
create table t_lottery_user_preapply 
(
   preApplyId           bigint                         not null auto_increment comment '预申请ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   userId               bigint                         not null comment '用户ID',
   partnerUniqueNo   	varchar(64)                    not null comment '渠道订单唯一号',
   preMoney             bigint                    	   not null comment '预存款金额',
   status           	smallint                       not null default 1 comment '申请状态: 1 待审核 2审核通过 3审核未通过',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (preApplyId),
   unique key partnerUniqueNo(partnerId, partnerUniqueNo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户预申请表';

create index t_lottery_user_preapply_userId_index on t_lottery_user_preapply(userId);
create index t_lottery_user_preapply_partnerNo_index on t_lottery_user_preapply(partnerUniqueNo);

drop table if exists t_lottery_user_id;
create table t_lottery_user_id(
    id               bigint                         not null auto_increment comment '用户ID',
    tableName        varchar(64)                    not null unique comment '表描述',
    primary key (id)
) DEFAULT CHARSET=utf8 comment '用户ID生成表';

drop table if exists t_lottery_handsel_daily_count;
create table t_lottery_handsel_daily_count
(
	day                  DATE                 NOT NULL DEFAULT '0000-00-00' COMMENT '统计时间',
	totalHandsel         bigint               not null default 0 comment '派发的彩金数额',  
	totalInvalidHandsel  bigint               not null default 0 comment '失效的彩金数额',  
	primary key (day)
) DEFAULT CHARSET=utf8 comment '彩金按日统计表';
