ALTER TABLE `t_lottery_partner` CHANGE secretKey secretKey VARCHAR(32) NOT NULL DEFAULT '' COMMENT '私钥文件名称'; 

ALTER TABLE `t_lottery_partner` ADD publicKey VARCHAR(32) NOT NULL DEFAULT '' COMMENT '公钥文件名称';

ALTER TABLE `t_lottery_partner` ADD alias VARCHAR(32) NOT NULL DEFAULT '' COMMENT '密钥别名';

ALTER TABLE `t_lottery_partner` ADD keyStore VARCHAR(32) NOT NULL DEFAULT '' COMMENT '密钥密码';