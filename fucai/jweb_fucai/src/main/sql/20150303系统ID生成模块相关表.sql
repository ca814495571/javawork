create table fucai_idgen.t_lottery_id_generate (
   idName			 varchar(32) NOT NULL comment 'ID名称',
   currentId         bigint(20) NOT NULL comment '当前ID',
   offset            int(11) NOT NULL comment '偏移量',
   lastUpdateTime    timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '最后更新时间',
   UNIQUE KEY(idName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='系统ID生成信息表';

INSERT INTO fucai_idgen.t_lottery_id_generate(idName,currentId,offset) VALUES('orderNo',1,300);