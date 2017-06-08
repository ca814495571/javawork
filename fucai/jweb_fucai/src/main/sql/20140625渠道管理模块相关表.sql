/* Table: 渠道商表 */
create table t_lottery_partner (
   partnerId           varchar(64) not null comment '渠道商id',
   partnerName         varchar(64) not null comment '渠道商名称',
   partnerType         bigint not null default 0 comment '渠道商类型：1纯B2B，2平台管理用户帐号，3平台管理用户帐号、用户账户 ',
   secretKey           char(32) not null default '0' comment '密钥',
   state               tinyint not null default 1 comment '渠道商状态: 1正常，2锁定',
   minBalance          bigint not null default 0 comment '余额告警值',
   callbackUrl         varchar(256) not null default '0' comment '回调url',
   ext                 text comment '扩展信息',
   registrationTime    datetime not null comment '创建时间',
   lastUpdateTime      timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (partnerId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道商表';

/* Table: 渠道商IP地址表 */
create table t_lottery_partner_ip (
   id                int not null auto_increment comment 'id',
   ipAddress         varchar(64) not null comment '渠道商IP地址',
   partnerId         varchar(64) not null comment '渠道商id',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '渠道商IP地址表';
