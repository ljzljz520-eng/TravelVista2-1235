# 旅游景点管理系统 - 毕业设计项目

## 项目介绍
基于Spring Boot + Vue 3的前后端分离旅游景点管理系统，包含用户端完整功能。

## 技术栈
### 后端
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3.1
- Sa-Token 1.34.0（权限认证）
- MySQL 8.0
- Hutool 工具集

### 前端
- Vue 3 + Vite
- Element Plus
- Vue Router 4
- Axios

## 功能模块
✅ **用户模块**：注册、登录、JWT认证、个人信息管理
✅ **景点模块**：列表展示、详情查看、搜索筛选、热门/推荐景点
✅ **评论模块**：发表评论、评分、点赞、景点评分自动计算
✅ **个人中心**：个人信息修改、我的评论管理
✅ **响应式布局**：适配不同屏幕尺寸

## 快速启动

### 1. 环境准备
- JDK 11+
- MySQL 8.0+
- Node.js 16+
- Maven 3.6+

### 2. 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE travel_vista DEFAULT CHARACTER SET utf8mb4;
```
2. 执行SQL脚本：`sql/travel_vista.sql`
3. 修改数据库配置：`travel-vista-server/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    username: 你的MySQL用户名
    password: 你的MySQL密码
```

### 3. 启动后端
```bash
cd travel-vista-server
mvn spring-boot:run
```
后端启动后访问：http://localhost:8080/api

### 4. 启动前端
```bash
cd travel-vista-web
npm install
npm run dev
```
前端启动后访问：http://localhost:5173

## 测试账号
- 普通用户：用户名 `test` / 密码 `test123`
- 管理员：用户名 `admin` / 密码 `admin123`

## 项目结构
```
TravelVista2-1235-0414_Original/
├── sql/                    # 数据库脚本
├── travel-vista-server/    # Spring Boot后端
│   ├── src/main/java/com/travelvista/
│   │   ├── entity/         # 实体类
│   │   ├── mapper/         # 数据访问层
│   │   ├── service/        # 业务逻辑层
│   │   ├── controller/     # 控制器
│   │   ├── config/         # 配置类
│   │   └── common/         # 公共工具
│   └── resources/
│       └── application.yml # 配置文件
└── travel-vista-web/       # Vue 3前端
    ├── src/
    │   ├── api/            # 接口请求
    │   ├── views/          # 页面组件
    │   ├── components/     # 公共组件
    │   ├── router/         # 路由配置
    │   ├── utils/          # 工具类
    │   └── App.vue         # 根组件
    └── package.json
```

## API接口
- 用户相关：`/api/user/**`
- 景点相关：`/api/scenic/**`
- 评论相关：`/api/review/**`

## 后续扩展功能（可选）
- 景点收藏功能
- 门票预订/订单功能
- 管理员后台管理
- 景点地图定位（集成高德/百度地图）
- 智能推荐算法
- Redis缓存优化
- 图片上传到云存储
