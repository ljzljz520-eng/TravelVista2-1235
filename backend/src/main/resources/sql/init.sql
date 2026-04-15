-- 创建数据库
CREATE DATABASE IF NOT EXISTS travel_vista DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE travel_vista;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    gender TINYINT DEFAULT 0 COMMENT '性别：0未知，1男，2女',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用，1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    role VARCHAR(20) DEFAULT 'admin' COMMENT '角色：admin超级管理员，manager普通管理员',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用，1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 景点分类表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(255) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点分类表';

-- 景点表
CREATE TABLE IF NOT EXISTS scenic_spot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '景点ID',
    name VARCHAR(100) NOT NULL COMMENT '景点名称',
    category_id BIGINT COMMENT '分类ID',
    description TEXT COMMENT '景点描述',
    content TEXT COMMENT '详细内容（富文本）',
    cover_image VARCHAR(255) COMMENT '封面图片',
    images TEXT COMMENT '图片列表，JSON数组',
    video VARCHAR(255) COMMENT '视频URL',
    address VARCHAR(255) COMMENT '详细地址',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    latitude DECIMAL(10,6) COMMENT '纬度',
    longitude DECIMAL(10,6) COMMENT '经度',
    price DECIMAL(10,2) DEFAULT 0.00 COMMENT '门票价格',
    open_time VARCHAR(100) COMMENT '开放时间',
    visit_time VARCHAR(50) COMMENT '建议游玩时长',
    rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '评分',
    review_count INT DEFAULT 0 COMMENT '评论数量',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    favorite_count INT DEFAULT 0 COMMENT '收藏数量',
    status TINYINT DEFAULT 1 COMMENT '状态：0下架，1上架',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热门：0否，1是',
    is_recommended TINYINT DEFAULT 0 COMMENT '是否推荐：0否，1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- 评论表
CREATE TABLE IF NOT EXISTS review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    scenic_id BIGINT NOT NULL COMMENT '景点ID',
    content TEXT COMMENT '评论内容',
    images TEXT COMMENT '图片列表，JSON数组',
    rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '评分',
    status TINYINT DEFAULT 1 COMMENT '状态：0审核中，1已通过，2已拒绝',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 收藏表
CREATE TABLE IF NOT EXISTS favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    scenic_id BIGINT NOT NULL COMMENT '景点ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_scenic (user_id, scenic_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    scenic_id BIGINT NOT NULL COMMENT '景点ID',
    scenic_name VARCHAR(100) COMMENT '景点名称',
    scenic_image VARCHAR(255) COMMENT '景点图片',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    visitor_name VARCHAR(50) COMMENT '游客姓名',
    visitor_phone VARCHAR(20) COMMENT '游客电话',
    visit_date DATE COMMENT '游玩日期',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0待支付，1已支付，2已取消，3已完成',
    pay_time DATETIME COMMENT '支付时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 插入初始数据
-- 插入管理员账号（密码：admin123，经过MD5加密）
INSERT INTO admin (username, password, nickname, role) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'admin');

-- 插入用户测试账号（密码：123456）
INSERT INTO user (username, password, nickname, email, phone, gender) VALUES 
('user1', 'e10adc3949ba59abbe56e057f20f883e', '旅行者小明', 'user1@example.com', '13800138001', 1),
('user2', 'e10adc3949ba59abbe56e057f20f883e', '旅游达人小红', 'user2@example.com', '13800138002', 2);

-- 插入分类数据
INSERT INTO category (name, description, sort_order) VALUES 
('自然风光', '山川湖泊、森林草原等自然景观', 1),
('历史古迹', '历史建筑、文化遗址', 2),
('主题乐园', '游乐园、主题公园', 3),
('博物馆', '展览馆、科技馆', 4),
('海滨度假', '海滩、海岛度假', 5);

-- 插入景点测试数据
INSERT INTO scenic_spot (name, category_id, description, content, cover_image, address, province, city, price, open_time, visit_time, rating, is_hot, is_recommended) VALUES 
('故宫博物院', 2, '中国明清两代的皇家宫殿，世界上现存规模最大的木质结构古建筑群', '故宫博物院是中国最大的古代文化艺术博物馆，建立于1925年10月10日。故宫又称紫禁城，是明、清两代的皇宫，也是古老中国的标志和象征。', 'https://example.com/gugong.jpg', '北京市东城区景山前街4号', '北京', '北京', 60.00, '08:30-17:00（周一闭馆）', '3-4小时', 4.9, 1, 1),
('长城', 2, '万里长城是中国古代的军事防御工程，世界文化遗产', '长城是中国也是世界上修建时间最长、工程量最大的一项古代防御工程，自西周时期开始，延续不断修筑了2000多年。', 'https://example.com/changcheng.jpg', '北京市延庆区八达岭镇', '北京', '北京', 45.00, '06:30-19:00', '4-5小时', 4.8, 1, 1),
('西湖', 1, '杭州西湖是中国著名的风景名胜区，世界文化遗产', '西湖，位于浙江省杭州市西面，是中国大陆首批国家重点风景名胜区和中国十大风景名胜之一。', 'https://example.com/xihu.jpg', '浙江省杭州市西湖区', '浙江', '杭州', 0.00, '全天开放', '1-2天', 4.9, 1, 1),
('黄山', 1, '黄山是世界文化与自然双重遗产，天下第一奇山', '黄山以奇松、怪石、云海、温泉、冬雪"五绝"著称于世，拥有"天下第一奇山"之称。', 'https://example.com/huangshan.jpg', '安徽省黄山市黄山区', '安徽', '黄山', 190.00, '06:00-18:00', '1-2天', 4.9, 1, 0),
('迪士尼乐园', 3, '上海迪士尼乐园是中国内地首座迪士尼主题乐园', '上海迪士尼乐园是一座神奇王国风格的迪士尼主题乐园，包含六个主题园区。', 'https://example.com/disney.jpg', '上海市浦东新区川沙镇黄赵路310号', '上海', '上海', 435.00, '08:30-21:30', '1天', 4.7, 1, 1);

-- 插入评论测试数据
INSERT INTO review (user_id, scenic_id, content, rating, status) VALUES 
(1, 1, '故宫真的太震撼了！建议大家提前预约，最好工作日去，人会少一些。里面的珍宝馆和钟表馆非常值得一看。', 5.0, 1),
(2, 1, '作为一个历史爱好者，故宫来了好几次了，每次都有新的收获。建议租个讲解器，能了解更多历史知识。', 4.5, 1),
(1, 3, '西湖的美景真的名不虚传，断桥残雪、苏堤春晓，每个景点都有动人的故事。推荐傍晚去看夕阳，太美了！', 5.0, 1);
