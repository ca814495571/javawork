-- --------------------------------------------------------
-- 主机:                           192.168.1.119
-- 服务器版本:                        5.1.73-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      unknown-linux-gnu
-- HeidiSQL 版本:                  8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 rdb.t_activity_record 结构
CREATE TABLE IF NOT EXISTS `t_activity_record` (
  `time` bigint(20) NOT NULL COMMENT '截止日期:20141012',
  `scratch10_receive_number` bigint(20) DEFAULT '0' COMMENT '10元刮刮乐领取数量',
  `scratch5_receive_number` bigint(20) DEFAULT '0' COMMENT '5元刮刮乐领取数量',
  `scratch_receive_totalMoney` bigint(20) DEFAULT '0' COMMENT '刮刮乐领取总金额',
  `actual_scratch5_exchange_number` bigint(20) DEFAULT '0' COMMENT '实际5元刮刮乐兑换数量',
  `actual_scratch10_exchange_number` bigint(20) DEFAULT '0' COMMENT '实际10元刮刮乐兑换数量',
  `actual_scratch_exchange_totalMoney` bigint(20) DEFAULT '0' COMMENT '实际刮刮乐兑换总金额',
  `scratch10_handsel_receive_number` bigint(20) DEFAULT '0' COMMENT '10元彩金领取数量',
  `scratch10_handsel_receive_money` bigint(20) DEFAULT '0' COMMENT '10元彩金领取金额',
  `address` varchar(200) NOT NULL COMMENT '投注站地址',
  `station_name` varchar(100) NOT NULL COMMENT '投注站名称',
  `bet_station` varchar(100) NOT NULL COMMENT '投注站编码',
  `if_join_activity` tinyint(4) DEFAULT '0' COMMENT '是否参加了活动 0未参加 1参加了',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_activity_scancode_source 结构
CREATE TABLE IF NOT EXISTS `t_activity_scancode_source` (
  `time` bigint(20) NOT NULL COMMENT '截止日期:20141012',
  `scancode_entrance_number` bigint(20) DEFAULT '0' COMMENT '扫码入口次数',
  `station_name` varchar(100) NOT NULL COMMENT '投注站名称',
  `address` varchar(200) NOT NULL COMMENT '投注站地址',
  `bet_station` varchar(100) NOT NULL COMMENT '投注站编码',
  `if_join_activity` tinyint(4) DEFAULT '0' COMMENT '是否有扫码 0未扫码 1已扫码',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_activity_summary 结构
CREATE TABLE IF NOT EXISTS `t_activity_summary` (
  `time` bigint(20) NOT NULL COMMENT '截止日期:20141012',
  `join_exchange_bet_station_number` bigint(20) NOT NULL DEFAULT '0' COMMENT '参与兑换的投注站数量',
  `all_scratch5_receive_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '所有投注站5元刮刮乐总领取数量',
  `all_scratch10_receive_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '所有投注站10元刮刮乐总领取数量',
  `all_bet_station_receive_total_money` bigint(20) NOT NULL DEFAULT '0' COMMENT '所有投注站刮刮乐领取总金额',
  `all_bet_station_scratch5_exchange_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '所有投注站刮刮乐5元兑换数量',
  `all_bet_station_scratch10_exchange_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '所有投注站刮刮乐10元兑换数量',
  `all_bet_station_exchange_total_money` bigint(20) NOT NULL DEFAULT '0' COMMENT '所有投注站刮刮乐兑换总金额',
  `handsel10_receive_total_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '10元彩金领取总数量',
  `handsel10_receive_total_money` bigint(20) NOT NULL DEFAULT '0' COMMENT '10元彩金领取总金额',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_branch 结构
CREATE TABLE IF NOT EXISTS `t_branch` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '当天订单数',
  `new_user` bigint(20) DEFAULT '0' COMMENT '当天新增用户',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_branch_lottery 结构
CREATE TABLE IF NOT EXISTS `t_branch_lottery` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `lottery` varchar(64) NOT NULL COMMENT '彩种',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '当天订单数',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`,`lottery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_branch_lottery_sum 结构
CREATE TABLE IF NOT EXISTS `t_branch_lottery_sum` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `lottery` varchar(64) NOT NULL COMMENT '彩种',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '截止到当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '截止到当天订单数',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`,`lottery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_branch_sum 结构
CREATE TABLE IF NOT EXISTS `t_branch_sum` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '截止到当天总销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '截止到当天总订单数',
  `new_user` bigint(20) DEFAULT '0' COMMENT '截止到当天总用户数',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_center 结构
CREATE TABLE IF NOT EXISTS `t_center` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '当天订单数',
  `new_user` bigint(20) DEFAULT '0' COMMENT '当天新增用户',
  `bet_station` varchar(32) NOT NULL COMMENT '中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_center_lottery 结构
CREATE TABLE IF NOT EXISTS `t_center_lottery` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `lottery` varchar(64) NOT NULL COMMENT '彩种',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '当天订单数',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`,`lottery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_center_lottery_sum 结构
CREATE TABLE IF NOT EXISTS `t_center_lottery_sum` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `lottery` varchar(64) NOT NULL COMMENT '彩种',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '截止到当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '截止到当天订单数',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`,`lottery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_center_sum 结构
CREATE TABLE IF NOT EXISTS `t_center_sum` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '截止到当天总销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '截止到当天总订单数',
  `new_user` bigint(20) DEFAULT '0' COMMENT '截止到当天总用户数',
  `bet_station` varchar(32) NOT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '分中心名称',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_deal 结构
CREATE TABLE IF NOT EXISTS `t_deal` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `content` text NOT NULL COMMENT '内容：下单时间##客户名称##彩种##注数##userId##订单id',
  `bet_station` varchar(32) NOT NULL COMMENT '投注站编码',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '投注站名称',
  `address` varchar(128) DEFAULT NULL COMMENT '投注站地址',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_stat 结构
CREATE TABLE IF NOT EXISTS `t_stat` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '当天订单数',
  `new_user` bigint(20) DEFAULT '0' COMMENT '当天新增用户',
  `bet_station` varchar(32) NOT NULL COMMENT '投注站编码',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '投注站名称',
  `address` varchar(128) DEFAULT NULL COMMENT '投注站地址',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_stat_lottery 结构
CREATE TABLE IF NOT EXISTS `t_stat_lottery` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `lottery` varchar(64) NOT NULL COMMENT '彩种',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '当天订单数',
  `bet_station` varchar(32) NOT NULL COMMENT '投注站编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '投注站名称',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '分中心编码',
  `address` varchar(128) DEFAULT NULL COMMENT '投注站地址',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`,`lottery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_stat_lottery_sum 结构
CREATE TABLE IF NOT EXISTS `t_stat_lottery_sum` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `lottery` varchar(64) NOT NULL COMMENT '彩种',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '截止到当天销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '截止到当天订单数',
  `bet_station` varchar(32) NOT NULL COMMENT '投注站编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '投注站名称',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '分中心编码',
  `address` varchar(128) DEFAULT NULL COMMENT '投注站地址',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`,`lottery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_stat_sum 结构
CREATE TABLE IF NOT EXISTS `t_stat_sum` (
  `time` bigint(20) NOT NULL COMMENT '日期:20141012',
  `pay_money` bigint(20) DEFAULT '0' COMMENT '截止到当天总销售额',
  `pay_cnt` bigint(20) DEFAULT '0' COMMENT '截止到当天总订单数',
  `new_user` bigint(20) DEFAULT '0' COMMENT '截止到当天总用户数',
  `bet_station` varchar(32) NOT NULL COMMENT '投注站编码',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '投注站名称',
  `address` varchar(128) DEFAULT NULL COMMENT '投注站地址',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 rdb.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `content` text NOT NULL COMMENT '内容：绑定时间##客户名称||绑定时间##客户名称',
  `bet_station` varchar(32) NOT NULL COMMENT '投注站编码',
  `parent_code` varchar(32) DEFAULT NULL COMMENT '分中心编码',
  `station_name` varchar(64) DEFAULT NULL COMMENT '投注站名称',
  `address` varchar(128) DEFAULT NULL COMMENT '投注站地址',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`bet_station`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
