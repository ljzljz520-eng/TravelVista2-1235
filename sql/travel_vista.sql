-- 旅游景点管理系统数据库
CREATE DATABASE IF NOT EXISTS travel_vista DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE travel_vista;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '管理员账号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `role` VARCHAR(20) DEFAULT 'admin' COMMENT '角色',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 3. 景点信息表
CREATE TABLE IF NOT EXISTS `scenic_spot` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '景点名称',
    `cover_image` VARCHAR(255) COMMENT '封面图URL',
    `images` TEXT COMMENT '景点图片列表，JSON格式',
    `video` VARCHAR(255) COMMENT '视频URL',
    `description` TEXT COMMENT '景点描述',
    `content` TEXT COMMENT '详细介绍，富文本',
    `province` VARCHAR(50) COMMENT '省份',
    `city` VARCHAR(50) COMMENT '城市',
    `address` VARCHAR(255) COMMENT '详细地址',
    `longitude` DECIMAL(10,7) COMMENT '经度',
    `latitude` DECIMAL(10,7) COMMENT '纬度',
    `type` VARCHAR(50) COMMENT '景点类型：自然风光、人文古迹、主题乐园等',
    `level` VARCHAR(20) COMMENT '景点等级：5A、4A等',
    `ticket_price` DECIMAL(10,2) DEFAULT 0 COMMENT '门票价格',
    `open_time` VARCHAR(255) COMMENT '开放时间',
    `visit_time` VARCHAR(100) COMMENT '建议游玩时间',
    `rating` DECIMAL(2,1) DEFAULT 0 COMMENT '评分，0-5分',
    `review_count` INT DEFAULT 0 COMMENT '评论数',
    `view_count` INT DEFAULT 0 COMMENT '浏览量',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_province_city` (`province`, `city`),
    KEY `idx_type` (`type`),
    KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点信息表';

-- 4. 评论表
CREATE TABLE IF NOT EXISTS `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `scenic_spot_id` BIGINT NOT NULL COMMENT '景点ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `images` TEXT COMMENT '评论图片，JSON格式',
    `rating` TINYINT NOT NULL COMMENT '评分，1-5分',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0待审核 1已通过 2已拒绝',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_scenic_spot_id` (`scenic_spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 5. 订单表
CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `scenic_spot_id` BIGINT NOT NULL COMMENT '景点ID',
    `scenic_spot_name` VARCHAR(100) NOT NULL COMMENT '景点名称',
    `ticket_price` DECIMAL(10,2) NOT NULL COMMENT '门票单价',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `visitor_name` VARCHAR(50) NOT NULL COMMENT '游客姓名',
    `visitor_phone` VARCHAR(20) NOT NULL COMMENT '游客手机号',
    `visit_date` DATE NOT NULL COMMENT '游玩日期',
    `status` TINYINT DEFAULT 0 COMMENT '订单状态 0待支付 1已支付 2已取消 3已完成',
    `pay_time` DATETIME COMMENT '支付时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_scenic_spot_id` (`scenic_spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 6. 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `scenic_spot_id` BIGINT NOT NULL COMMENT '景点ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_scenic` (`user_id`, `scenic_spot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 插入默认管理员账号
INSERT INTO `admin` (`username`, `password`, `nickname`) VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJfwW', '超级管理员');
-- 默认密码：admin123

-- 插入测试用户
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`) VALUES ('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJfwW', '测试用户', '13800138000');
-- 默认密码：test123