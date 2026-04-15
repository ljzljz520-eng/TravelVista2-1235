CREATE DATABASE IF NOT EXISTS travel_vista DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE travel_vista;

DROP TABLE IF EXISTS `favorite`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `scenic_spot`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `admin`;

CREATE TABLE `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `gender` TINYINT DEFAULT 0 COMMENT '性别 0未知 1男 2女',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记 0未删除 1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `admin` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `role` VARCHAR(50) DEFAULT 'admin' COMMENT '角色',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

CREATE TABLE `scenic_spot` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '景点ID',
  `name` VARCHAR(100) NOT NULL COMMENT '景点名称',
  `description` TEXT COMMENT '景点描述',
  `images` TEXT COMMENT '景点图片，多个用逗号分隔',
  `cover_image` VARCHAR(255) COMMENT '封面图',
  `province` VARCHAR(50) COMMENT '省份',
  `city` VARCHAR(50) COMMENT '城市',
  `address` VARCHAR(255) COMMENT '详细地址',
  `latitude` DECIMAL(10, 6) COMMENT '纬度',
  `longitude` DECIMAL(10, 6) COMMENT '经度',
  `type` VARCHAR(50) COMMENT '景点类型',
  `level` VARCHAR(20) COMMENT '景点等级',
  `ticket_price` DECIMAL(10, 2) DEFAULT 0 COMMENT '门票价格',
  `open_time` VARCHAR(100) COMMENT '开放时间',
  `phone` VARCHAR(50) COMMENT '联系电话',
  `rating` DECIMAL(3, 2) DEFAULT 5.0 COMMENT '评分',
  `view_count` INT DEFAULT 0 COMMENT '浏览量',
  `review_count` INT DEFAULT 0 COMMENT '评论数',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

CREATE TABLE `review` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `scenic_id` BIGINT NOT NULL COMMENT '景点ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `images` TEXT COMMENT '评论图片，多个用逗号分隔',
  `rating` TINYINT NOT NULL COMMENT '评分 1-5',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0审核不通过 1审核通过',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '删除标记'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

CREATE TABLE `favorite` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `scenic_id` BIGINT NOT NULL COMMENT '景点ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY `uk_user_scenic` (`user_id`, `scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

INSERT INTO `admin` (`username`, `password`, `nickname`, `role`) VALUES
('admin', 'admin123', '超级管理员', 'super_admin');

INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `gender`) VALUES
('zhangsan', '123456', '张三', 'zhangsan@example.com', 1),
('lisi', '123456', '李四', 'lisi@example.com', 2);

INSERT INTO `scenic_spot` (`name`, `description`, `cover_image`, `province`, `city`, `address`, `type`, `level`, `ticket_price`, `open_time`, `rating`, `view_count`, `review_count`) VALUES
('故宫博物院', '故宫博物院是中国明清两代的皇家宫殿，旧称紫禁城，位于北京中轴线的中心。故宫以三大殿为中心，占地面积约72万平方米，建筑面积约15万平方米，有大小宫殿七十多座，房屋九千余间。', 'https://picsum.photos/800/600?random=1', '北京', '北京', '东城区景山前街4号', '历史文化', '5A', 60.00, '08:30-17:00', 4.8, 15680, 2365),
('八达岭长城', '八达岭长城，位于北京市延庆区军都山关沟古道北口。是中国古代伟大的防御工程万里长城的重要组成部分，是明长城的一个隘口。', 'https://picsum.photos/800/600?random=2', '北京', '北京', '延庆区八达岭镇', '历史文化', '5A', 40.00, '06:30-19:00', 4.7, 23450, 3567),
('西湖', '西湖，位于浙江省杭州市西湖区龙井路1号，杭州市区西部，景区总面积49平方千米，汇水面积为21.22平方千米，湖面面积为6.38平方千米。', 'https://picsum.photos/800/600?random=3', '浙江', '杭州', '西湖区龙井路1号', '自然风光', '5A', 0.00, '全天开放', 4.9, 35680, 5689),
('黄山', '黄山，古称黟山，位于安徽省黄山市境内，地处安徽省南部、黄山市北部，地跨歙县、休宁县、黟县和黄山区、徽州区，山境南北长约40千米，东西宽约30千米，总占地面积约1200平方千米。', 'https://picsum.photos/800/600?random=4', '安徽', '黄山', '黄山区汤口镇', '自然风光', '5A', 190.00, '06:00-17:30', 4.8, 28900, 4123),
('九寨沟', '九寨沟国家级自然保护区，位于四川省西北部岷山山脉南段的阿坝藏族羌族自治州九寨沟县漳扎镇境内，地处岷山南段弓杆岭的东北侧。', 'https://picsum.photos/800/600?random=5', '四川', '阿坝', '九寨沟县漳扎镇', '自然风光', '5A', 169.00, '07:00-18:00', 4.9, 32100, 4890),
('秦始皇兵马俑博物馆', '秦始皇兵马俑博物馆位于陕西省西安市临潼区秦陵镇，是以秦始皇兵马俑为基础，在兵马俑坑原址上建立的遗址类博物馆，也是中国最大的古代军事博物馆。', 'https://picsum.photos/800/600?random=6', '陕西', '西安', '临潼区秦陵镇', '历史文化', '5A', 120.00, '08:30-17:00', 4.8, 26780, 3890);
