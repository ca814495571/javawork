drop table if exists t_lottery_role;

/* 角色表 */
create table t_lottery_role(
   roleId             int not null auto_increment comment '角色id',
   roleName           varchar(32) not null comment '角色名称',
   deleteFlag         tinyint not null default 0 comment '删除标识: 0未删除，1删除',
   lastUpdateTime     timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   primary key (roleId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
