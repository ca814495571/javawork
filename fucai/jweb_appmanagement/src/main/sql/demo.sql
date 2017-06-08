CREATE DATABASE IF NOT EXISTS demo default charset utf8 COLLATE utf8_general_ci;

create table samplemodel (
    id int unsigned not null auto_increment primary key,
    lastName varchar(40) not null,
    firstName varchar(40) not null,
    email varchar(80) not null,
    unique index subscriber_idx1 (email)
) engine = InnoDb;

insert into samplemodel(lastName, firstName, email) values ("James", "Gosling", "james.gosling@java.com");
insert into samplemodel(lastName, firstName, email) values ("Rod", "Johnson", "rod.johnson@spring.io");
insert into samplemodel(lastName, firstName, email) values ("Michael", "Monty", "michael.monty@mysql.com");

CREATE TABLE `t_user_info` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户名称',
	`password` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户密码',
	`stationCode` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '投注站编号',
	`roleId` INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '角色Id',
	`loginFailCount` INT(11) NOT NULL DEFAULT '0' COMMENT '登录失败次数',
	`createTime` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
	`loginTime` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '登录的时间',
	`lastUpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
	`active` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '删除标识符',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `unique_name` (`name`),
	INDEX `FK_t_user_info_t_role` (`roleId`),
	CONSTRAINT `FK_t_user_info_t_role` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`)
)
COMMENT='管理后台的用户信息表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=77;





CREATE TABLE `t_user_count` (
	`userTotalNum` INT(11) NOT NULL DEFAULT '0' COMMENT '当前用户总数量',
	`userDailyAddNum` INT(11) NOT NULL DEFAULT '0' COMMENT '日用户增量（当前时间与昨天凌晨00：00之前最后一次记录用户总数量的差值）',
	`countTime` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '统计时间（YYYY-MM-DD）',
	`year` INT(11) NOT NULL DEFAULT '0' COMMENT '统计的年份',
	`month` INT(11) NOT NULL DEFAULT '0' COMMENT '统计的月份',
	`stationId` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '外键关联到表t_station_info',
	`lastTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后改修时间',
	`enterTag` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '入口标识',
	PRIMARY KEY (`countTime`, `stationId`),
	INDEX `FK_userCount_station` (`stationId`),
	CONSTRAINT `FK_userCount_station` FOREIGN KEY (`stationId`) REFERENCES `t_station_info` (`id`)
)
COMMENT='用户数量统计表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


CREATE TABLE `t_station_map` (
	`id` VARCHAR(50) NOT NULL DEFAULT '',
	`address` VARCHAR(50) NOT NULL DEFAULT '',
	`phone` VARCHAR(50) NOT NULL DEFAULT '',
	`lon` VARCHAR(50) NOT NULL DEFAULT '',
	`lat` VARCHAR(50) NOT NULL DEFAULT ''
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


CREATE TABLE `t_station_info` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
	`parentId` INT(11) UNSIGNED NOT NULL COMMENT '父站点ID',
	`stationName` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '站点名称',
	`stationLinkman` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点联系人',
	`stationTel` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点联系方式',
	`stationAddOne` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点所属区地址（例：渝中区）',
	`stationAddTwo` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '站点所属区地址后的详细地址',
	`stationCode` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点站号',
	`stationOrgLevel` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点组织级别',
	`stationOrg` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点所属组织',
	`stationLongitude` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点百度地图经度',
	`stationLatitude` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点百度地图纬度',
	`stationAccountNum` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点帐号',
	`stationPassword` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '站点密码',
	`stationCreateTime` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '站点创建时间',
	`stationFlag` INT(11) NOT NULL DEFAULT '0' COMMENT '站点标识（1中心.2分中心.3销售站）',
	`lastTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
	`parentStationName` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '父站点名称',
	`enterTag` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '投注站的入口标识',
	`active` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '记录是否有效标识（1有效，2无效）',
	PRIMARY KEY (`id`, `enterTag`),
	INDEX `Index_parentId` (`parentId`),
	CONSTRAINT `FK_station_parentId` FOREIGN KEY (`parentId`) REFERENCES `t_station_info` (`id`)
)
COMMENT='彩票站点信息表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=298;





CREATE TABLE `t_role_function` (
	`roleId` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	`url` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '能访问的URL',
	`lastUpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	INDEX `FK_t_role_function_t_role` (`roleId`),
	CONSTRAINT `FK_t_role_function_t_role` FOREIGN KEY (`roleId`) REFERENCES `t_role` (`id`)
)
COMMENT='角色功能表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;






CREATE TABLE `t_role` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`roleName` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '角色名称',
	`lastUpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
	PRIMARY KEY (`id`)
)
COMMENT='角色表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5;




CREATE TABLE `t_lottery_plan` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`planId` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '订单方案的唯一标识',
	`userId` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '用户Id',
	`lotteryId` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '彩票ID',
	`lotteryName` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '彩票名称',
	`totalAmount` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '购买总金额',
	`createTime` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '购买时间',
	`charOne` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '入口标识',
	`charTwo` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户来源属性tag',
	`extInfo` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '扩展信息',
	`lastUpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COMMENT='对应FTP 下 planinfo**.txt 的数据'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=93;






CREATE TABLE `t_lottery_count` (
	`lotteryType` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '彩票种类',
	`lotteryDailyNum` INT(11) NOT NULL DEFAULT '0' COMMENT '彩种日销售数量',
	`lotteryMonthNum` INT(11) NOT NULL DEFAULT '0' COMMENT '彩种月销售数量（前一天月销售数量加上日销售量数）',
	`countTime` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '统计时间',
	`year` INT(11) NOT NULL DEFAULT '0' COMMENT '统计年份',
	`month` INT(11) NOT NULL DEFAULT '0' COMMENT '统计月份',
	`stationId` INT(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '外键关联到表t_station_info',
	`lastTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次修改时间',
	`enterTag` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '入口标识',
	PRIMARY KEY (`countTime`, `stationId`, `lotteryType`),
	INDEX `FK_lotteryCount_station` (`stationId`),
	CONSTRAINT `FK_lotteryCount_station` FOREIGN KEY (`stationId`) REFERENCES `t_station_info` (`id`)
)
COMMENT='彩票销售数量统计表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;



CREATE TABLE `t_ftp_user_info` (
	`id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`userId` VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '用户Id',
	`ext` VARCHAR(200) NOT NULL DEFAULT '' COMMENT '扩展字段中包含入口标识',
	`registTime` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '注册时间',
	`lastUpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COMMENT='对应FTP userinfo**.txt 中的数据'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2326;



CREATE TABLE `t_ftp_file_log` (
	`fileName` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '文件名',
	`createTime` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '文件下载的时间',
	`flag` TINYINT(4) NOT NULL DEFAULT '2' COMMENT '标识文件是否被成功读取（1已读 2未读）',
	`downTime` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '文件下载的日期',
	`lastUpdateTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`fileName`)
)
COMMENT='ftp下载文件日志'
COLLATE='utf8_general_ci'
ENGINE=MyISAM;




