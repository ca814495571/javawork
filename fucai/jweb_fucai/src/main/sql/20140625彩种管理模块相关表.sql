/* 开奖结果表 */
create table t_lottery_draw_result (
   lotteryId         varchar(32) not null comment '彩种Id',
   issueNo           varchar(64) not null comment '期号',
   drawResult        varchar(128) not null comment '开奖结果',
   state             tinyint not null default 1 comment '有效状态: 0无效 1有效',
   priority          tinyint not null default 0 comment '优先级',
   ext               text comment '扩展信息',
   createTime        datetime comment '创建时间',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (lotteryId, issueNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='开奖结果表';

/* 奖级信息表 */
create table t_lottery_draw_level (
   levelId			 int not null auto_increment comment '奖级id',
   lotteryId         varchar(32) not null comment '彩种Id',
   issueNo           varchar(64) not null comment '期号',
   level        	 tinyint not null comment '奖级',
   levelName         varchar(64) not null comment '奖级名称',
   money             bigint not null comment '该奖级的奖金',
   totalCount        bigint not null comment '该奖级的中奖人数',
   createTime        datetime comment '创建时间',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (levelId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='奖级信息表';

/* 彩种期号表 */
create table t_lottery_issue (
   issueId                 int not null auto_increment comment '期号id',
   lotteryId               varchar(32) not null comment '彩种Id',
   issueNo                 varchar(64) not null comment '期号',
   drawResult              varchar(128) comment '开奖结果',
   state                   tinyint  not null default 1 comment '期号状态:1未销售 2销售中 3销售截止 4已保底 5已出票 6已撤单 7已转移 8已开奖 9过关中 10已过关 11过关已审核 12算奖中 13已算奖待审核 14算奖已审核 16派奖中 17已派奖',
   drawTime                datetime not null comment '开奖时间',
   beginTime               datetime not null comment '投注开始时间',
   singleEndTime           datetime not null comment '单式自购截止时间',
   compoundEndTime         datetime not null comment '复式自购截止时间',
   singleTogetherEndTime   datetime not null comment '单式合买截止时间',
   compoundTogetherEndTime datetime not null comment '复式合买截止时间',
   singleUploadEndTime 	   datetime not null comment '单式上传截止时间',
   printBeginTime 		   datetime not null comment '出票开始时间',
   printEndTime 		   datetime not null comment '出票截止时间',
   officialBeginTime 	   datetime not null comment '官方销售开始时间',
   officialEndTime 		   datetime not null comment '官方销售截止时间',
   prizePool               bigint not null default 0 comment '奖池滚存',
   ext                     text comment '扩展信息',
   createTime        	   datetime comment '创建时间',
   lastUpdateTime          timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (issueId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='彩种期号表';

/* unique索引: idx_lottery_issue */
create unique index idx_lottery_issue on t_lottery_issue (
   lotteryId,
   issueNo
);

/* 彩种表 */
create table t_lottery_item (
   id				  int not null auto_increment comment 'id',
   lotteryId          varchar(32) not null comment '彩种id',
   lotteryName        varchar(64) not null comment '彩种名称',
   lotteryType        tinyint not null default 0 comment '彩种类型: 1双色球，2福彩3D，3七乐彩，4幸运农场，5时时彩',
   createTime         datetime comment '创建时间',
   lastUpdateTime     timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='彩种表';

/* 期状态扫描任务完成表 */
create table t_lottery_task_complete (
   id				  int not null auto_increment comment 'id',
   lotteryId          varchar(32) not null comment '彩种id',
   issueNo        	  varchar(64) not null comment '期号',
   taskType        	  tinyint not null comment '任务类型: 6已撤单 13已算奖待审核 15算奖已审核  17已派奖',
   setNo			  varchar(32) not null comment '机器编号',
   createTime         datetime comment '创建时间',
   lastUpdateTime     timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (id),
   UNIQUE KEY uk (lotteryId,issueNo,taskType,setNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='期状态扫描任务完成表';

/* 开奖源表 */
create table t_lottery_draw_src (
   srcNo             varchar(32) not null comment '开奖源编号',
   srcName           varchar(64) not null comment '开奖源名',
   srcState          tinyint not null default 1 comment '开奖源状态:0:无效 1有效（默认为1）',
   priority          tinyint not null default 0 comment '优先级',
   srcDesc           text comment '开奖源描述',
   ext               text comment '扩展信息',
   createTime        datetime comment '创建时间',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (srcNo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='开奖源表';
