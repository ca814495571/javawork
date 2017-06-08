drop table if exists t_lottery_activity;

/* Table: 活动配置表 */
create table t_lottery_activity (
   activityId        bigint not null auto_increment comment '活动id',
   activityName      varchar(64) not null default '' comment '活动名称',
   startTime         datetime not null default '0000-00-00 00:00:00' comment '活动开始时间',
   endTime           datetime not null default '0000-00-00 00:00:00' comment '活动截止时间',
   activityType      bigint default 0 comment '活动类型',
   state             int default 0 comment '活动状态',
   ruleDesc          text comment '活动规则详情',
   bonusDesc         text comment '活动奖励详情',
   ext               text comment '扩展信息',
   createTime        datetime not null default '0000-00-00 00:00:00' comment '活动创建时间',
   lastModifier      varchar(255) comment '最后修改人',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (activityId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
