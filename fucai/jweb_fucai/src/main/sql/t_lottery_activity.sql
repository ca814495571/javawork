drop table if exists t_lottery_activity;

/* Table: ����ñ� */
create table t_lottery_activity (
   activityId        bigint not null auto_increment comment '�id',
   activityName      varchar(64) not null default '' comment '�����',
   startTime         datetime not null default '0000-00-00 00:00:00' comment '���ʼʱ��',
   endTime           datetime not null default '0000-00-00 00:00:00' comment '���ֹʱ��',
   activityType      bigint default 0 comment '�����',
   state             int default 0 comment '�״̬',
   ruleDesc          text comment '���������',
   bonusDesc         text comment '���������',
   ext               text comment '��չ��Ϣ',
   createTime        datetime not null default '0000-00-00 00:00:00' comment '�����ʱ��',
   lastModifier      varchar(255) comment '����޸���',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '������ʱ��',
   primary key (activityId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
