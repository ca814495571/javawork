drop table if exists t_lottery_award_record;


/* 彩金发放记录表 */
create table t_lottery_award_record (
   awardRecordId      bigint not null comment  '彩金发放记录id',
   userId             bigint not null comment  '用户id',
   partnerId          tinyint not null comment '渠道id',
   bonusId            varchar(255) comment '奖品id',
   bonusName          varchar(255) comment '奖品名称',
   seqNo              varchar(255) not null comment '流水号(sequence number)',
   awardedAmount      bigint not null comment '赠送的金额',
   awardedTime        datetime not null comment '赠送的时间',
   effectStartTime    datetime not null comment '有效期开始时间',
   effectEndTime      datetime not null comment '有效期结束时间',
   state              int not null comment '赠送的状态',
   ext                text comment '扩展信息',
   createTime         datetime comment '该记录的创建时间',
   lastUpdateTime     timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (awardRecordId)
);