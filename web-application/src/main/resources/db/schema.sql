DROP TABLE IF EXISTS `ai_conversation`;
CREATE TABLE `ai_conversation`
(
    `id`           INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '会话id',
    `title`        VARCHAR(256) NOT NULL COMMENT '会话名称',
    `llm_model`    VARCHAR(256) NULL COMMENT '会话使用模型',
    `ext`          mediumtext NULL COMMENT '扩展信息',
    `gmt_create`   DATETIME     NOT NULL DEFAULT NOW() COMMENT '创建时间',
    `gmt_modified` DATETIME     NOT NULL DEFAULT NOW() COMMENT '上次更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话信息表';

DROP TABLE IF EXISTS `ai_conversation_chat_detail`;
CREATE TABLE `ai_conversation_chat_detail`
(
    `id`              INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    `conversation_id` INT(11) NOT NULL COMMENT '会话id',
    `role`            VARCHAR(256) NOT NULL COMMENT '会话角色',
    `type`            VARCHAR(128) NOT NULL COMMENT '聊天类型：TEXT/IMAGE/VIDEO',
    `content`         mediumtext   NOT NULL COMMENT '对话内容',
    `ext`             mediumtext NULL COMMENT '扩展信息',
    `gmt_create`      DATETIME     NOT NULL DEFAULT NOW() COMMENT '创建时间',
    `gmt_modified`    DATETIME     NOT NULL DEFAULT NOW() COMMENT '上次更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话聊天详情表';

