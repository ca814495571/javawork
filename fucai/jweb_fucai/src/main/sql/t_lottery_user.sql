/*==============================================================*/
/* Table: t_lottery_user_info                                   */
/*==============================================================*/
drop table if exists t_lottery_user_info_{idx};
/* 用户表: 以用户id分表 */
create table t_lottery_user_info_{idx} (
   userId               bigint                         not null comment '用户ID',
   nickName             varchar(64)                    null comment '用户昵称',
   cardType             tinyint                        not null default 1 comment '证件类型:1身份证，2护照，3军官证，4港台同胞证',
   cardNo               varchar(64)                    null comment '证件号码',
   mobile               varchar(16)                    null comment '手机号',
   email                varchar(64)                    null comment 'email',
   userName             varchar(64)                    null comment '姓名',
   sex                  tinyint                        null comment '性别',
   birthday             datetime                      null comment '生日：由身份证号计算得出',
   age                  tinyint                        null comment '年龄：由身份证号计算得出',
   userType             tinyint                        null comment '用户类型',
   registerTerminal     tinyint                    	   not null default 2 comment '注册终端:1：手机 2：PC',
   accountType          tinyint                        not null default 0 comment '帐号类型:0/1：qq 2：wx',
   partnerId            varchar(64)                    not null comment '来源渠道ID',
   partnerUserId        varchar(64)                    not null comment '渠道用户ID',
   prizePassword        varchar(64)                    null default '' comment '用户账户兑奖密码',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (userId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户资料信息(总表)';

/*==============================================================*/
/* Table: t_lottery_user_account                                */
/*==============================================================*/
drop table if exists t_lottery_user_account_{idx};
create table t_lottery_user_account_{idx}
(
   userId               bigint                         not null comment '用户id',
   totalAmount          bigint                         not null default 0 comment '总金额=可用金额+冻结金额',
   freezeAmount         bigint                         not null default 0 comment '冻结金额',
   state                smallint                       not null default 1 comment '状态: 1正常 2冻结',
   usableAmount         bigint                         not null default 0 comment '可用金额',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (userId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户账户表';
/*==============================================================*/
/* Table: t_lottery_account_log                                 */
/*==============================================================*/
drop table if exists t_lottery_account_log_{idx};
create table t_lottery_account_log_{idx}
(
   logId                bigint                         not null auto_increment comment '流水ID',
   userId               bigint                         not null comment '用户ID',
   nickName             varchar(64)                    null comment '用户昵称',
   partnerId            varchar(64)                    not null comment '渠道ID',
   logType              smallint                       not null default 0 comment '收支状态：1充值  2支付  3提现  4退款  5派奖  6彩金赠送  7彩金失效  8冻结金额 9用户预存款',
   totalAmount          bigint                         not null comment '总金额',
   accountAmount        bigint                         not null default 0 comment '账户金额',
   handselAmount        bigint                         not null default 0 comment '彩金金额',
   serialNumber         varchar(64)                    not null comment '流水号',
   ext         			varchar(256)                   null comment '扩展信息',
   remark               varchar(256)                   null comment '备注',
   handselRemark        varchar(256)                   null comment '格式：彩金ID=金额，彩金ID=金额...',
   createTime           datetime                       not null comment '创建时间',
   primary key (logId),
   unique key(serialNumber)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户账户流水表';

create index t_lottery_account_log_userId_index_{idx} on t_lottery_account_log_{idx}(userId);
create index t_lottery_account_log_createTime_index_{idx} on t_lottery_account_log_{idx}(createTime);

/*==============================================================*/
/* Table: t_lottery_user_recharge                             */
/*==============================================================*/
drop table if exists t_lottery_user_recharge_{idx};
create table t_lottery_user_recharge_{idx} 
(
   rechargeId           bigint                         not null auto_increment comment '充值ID',
   userId               bigint                         not null comment '用户ID',
   nickName             varchar(64)                    null comment '用户昵称',
   partnerId            varchar(64)                    not null comment '渠道ID',
   serialNumber         varchar(64)                    not null unique comment '流水号',
   partnerRechargeId    varchar(64)                    not null comment '合作商充值ID',
   rechargeAmount       bigint                         not null comment '充值金额',
   rechargeType         varchar(64)                    null comment '充值方式',
   remark               varchar(256)                   null comment '备注',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (rechargeId),
   unique key partnerRechargeId(partnerId, partnerRechargeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '充值记录表';

create index t_lottery_user_recharge_userId_index_{idx} on t_lottery_user_recharge_{idx}(userId);
create index t_lottery_user_recharge_partner_index_{idx} on t_lottery_user_recharge_{idx}(partnerId);

/*==============================================================*/
/* Table: t_lottery_user_handsel                                */
/*==============================================================*/
drop table if exists t_lottery_user_handsel_{idx};
create table t_lottery_user_handsel_{idx} 
(
   handselId            bigint                         not null auto_increment comment '彩金ID',
   userId               bigint                         not null comment '用户ID',
   partnerId            varchar(64)                    not null comment '渠道ID',
   activityId           varchar(32)                    null comment '活动ID',
   partnerHandselId     varchar(64)                    not null unique comment '彩金交易在合作商的id',
   serialNumber         varchar(64)                    not null comment '流水号',
   presentAmount        bigint                         not null comment '赠送金额',
   usableAmount         bigint                         not null comment '可使用金额',
   usedAmount        	bigint                         not null comment '已使用金额',
   presentTime          datetime                       not null comment '赠送时间',
   validTime      		datetime                       null comment '有效时间',
   failureTime        	datetime                       null comment '失效时间',
   state                smallint                       not null default 1 comment '状态：1有效  2无效',
   version              bigint                         not null default 0 comment '版本号，用于更新时比较',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (handselId),
   unique key partnerHandselId(partnerId, partnerHandselId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户彩金表';

create index t_lottery_user_handsel_userId_index_{idx} on t_lottery_user_handsel_{idx}(userId);
create index t_lottery_user_handsel_state_index_{idx} on t_lottery_user_handsel_{idx}(state);
create index t_lottery_user_handsel_validTime_index_{idx} on t_lottery_user_handsel_{idx}(validTime);
create index t_lottery_user_handsel_failureTime_index_{idx} on t_lottery_user_handsel_{idx}(failureTime);


/*==============================================================*/
/* Table: t_lottery_withdraw_account                            */
/*==============================================================*/
drop table if exists t_lottery_withdraw_account_{idx};
create table t_lottery_withdraw_account_{idx} 
(
   withdrawAccountId    bigint                         not null auto_increment comment '提款帐号ID',
   userId               bigint                         not null comment '用户ID',
   realName             varchar(64)                    null comment '用户昵称',
   accountType          smallint                       null comment '支付帐号类型：1:银行卡 2:cft 3:支付宝',
   accountNo            varchar(64)                    null comment '帐号',
   bankType             varchar(64)                    null comment '银行类型',
   bankName             varchar(64)                    null comment '银行名称',
   bankCardType         smallint                       null comment '银行卡类型：1:信用卡 2:借记卡',
   payAccountState      smallint                       null comment '支付帐号状态：1:未绑定 2:已绑定',
   accountAddress       varchar(256)                   null comment '帐号地址信息',
   createTime           datetime                       not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (withdrawAccountId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户提款帐号信息表';

create index t_lottery_withdraw_account_userId_index_{idx} on t_lottery_withdraw_account_{idx}(userId);
create index t_lottery_withdraw_account_accountNo_index_{idx} on t_lottery_withdraw_account_{idx}(accountNo);