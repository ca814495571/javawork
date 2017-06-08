drop table if exists t_lottery_privilege;

/* 权限表 */
create table t_lottery_privilege (
   privilegeId       int not null auto_increment comment '权限Id',
   privilegeName     varchar(32) not null comment '权限名',
   deleteFlag        tinyint not null default 0 comment '删除标识: 0未删除，1删除',
   lastUpdateTime    timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (privilegeId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;