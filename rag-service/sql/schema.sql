CREATE TABLE `ai_context`
(
    `id`           INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `text`         mediumtext NULL COMMENT '扩展信息',
    `gmt_create`   DATETIME NOT NULL DEFAULT NOW() COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL DEFAULT NOW() COMMENT '上次更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI资源库';