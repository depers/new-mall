DROP TABLE IF EXISTS `pay_orders`;
CREATE TABLE `pay_orders`
(
    `id`                varchar(64)  NOT NULL COMMENT '订单主键',
    `merchant_order_id` varchar(64)  NOT NULL COMMENT '商户订单号',
    `merchant_user_id`  varchar(64)  NOT NULL COMMENT '商户方的发起用户的用户主键id',
    `amount`            int(11)      NOT NULL COMMENT '实际支付总金额（包含商户所支付的订单费邮费总额）',
    `pay_method`        int(11)      NOT NULL COMMENT '支付方式',
    `pay_status`        int(11)      NOT NULL COMMENT '支付状态 10：未支付 20：已支付 30：支付失败 40：已退款',
    `come_from`         varchar(128) NOT NULL COMMENT '从哪一端来的，比如从天天吃货这门实战过来的',
    `return_url`        varchar(255) NOT NULL COMMENT '支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址',
    `is_delete`         int(11)      NOT NULL COMMENT '逻辑删除状态;1: 删除 0:未删除',
    `created_time`      datetime     NOT NULL COMMENT '创建时间（成交时间）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表;';

CREATE TABLE `users`
(
    `id`           varchar(64)   NOT NULL COMMENT '主键id 用户id',
    `username`     varchar(32)   NOT NULL COMMENT '用户名 用户名',
    `password`     varchar(64)   NOT NULL COMMENT '密码 密码',
    `nickname`     varchar(32)  DEFAULT NULL COMMENT '昵称 昵称',
    `realname`     varchar(128) DEFAULT NULL COMMENT '真实姓名',
    `face`         varchar(1024) NOT NULL COMMENT '头像',
    `mobile`       varchar(32)  DEFAULT NULL COMMENT '手机号 手机号',
    `email`        varchar(32)  DEFAULT NULL COMMENT '邮箱地址 邮箱地址',
    `sex`          int          DEFAULT NULL COMMENT '性别 性别 1:男  0:女  2:保密',
    `birthday`     date         DEFAULT NULL COMMENT '生日 生日',
    `created_time` datetime      NOT NULL COMMENT '创建时间 创建时间',
    `updated_time` datetime      NOT NULL COMMENT '更新时间 更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表 ';