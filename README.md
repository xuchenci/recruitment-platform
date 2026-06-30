# 数智化校企招聘与就业生态平台

## 项目简介

基于Spring Cloud Alibaba微服务架构的招聘平台后端系统，使用SpringAI提供智能职位推荐功能。

## 技术栈

- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **Spring Cloud Alibaba**: 2023.0.1.0
- **MyBatis Plus**: 3.5.5
- **MySQL**: 8.0.33
- **Redis**: 用于缓存和Session存储
- **RocketMQ**: 消息队列，用于异步处理短信发送
- **Spring AI**: 1.0.0，用于智能推荐
- **Gateway**: Spring Cloud Gateway，API网关
- **Nacos**: 服务注册与配置中心
- **Sentinel**: 流量控制和熔断降级

## 微服务架构

```
recruitment-platform/
├── common-service          # 公共模块
├── gateway-service         # 网关服务（8080）
├── auth-service          # 认证服务（8081）
├── student-service       # 学生服务（8082）
├── enterprise-service    # 企业服务（8083）
├── job-service          # 职位服务（8084）
├── resume-service       # 简历服务（8085）
├── application-service  # 申请服务（8086）
├── recommendation-service # 智能推荐服务（8087）
├── notification-service # 通知服务（8088）
├── sms-service         # 短信发送服务（8089）
└── analytics-service    # 数据分析服务（8090）
```

## 功能模块

### 1. 用户认证模块
- 用户注册（学生、企业、学校）
- 用户登录
- JWT Token认证
- 短信验证码
- 密码重置

### 2. 学生模块
- 学生信息管理
- 简历创建与管理
- 技能标签管理
- 求职状态管理
- 简历完整度评分

### 3. 企业模块
- 企业信息管理
- 企业认证
- 职位发布与管理

### 4. 职位模块
- 职位发布
- 职位搜索（关键词、城市、薪资、分类）
- 热门职位推荐
- 职位浏览量统计

### 5. 简历模块
- 简历创建
- 教育经历管理
- 工作经历管理
- 项目经验管理
- 简历文件上传（PDF）

### 6. 申请模块
- 职位申请
- 申请状态跟踪
- 面试安排
- 申请历史查询

### 7. 智能推荐模块（SpringAI）
- 基于学生画像的职位推荐
- 职业发展规划建议
- 简历优化建议
- 面试准备指导
- 向量搜索相似职位

### 8. 通知模块
- 短信验证码发送
- 面试通知
- 申请状态更新通知
- RocketMQ异步处理

### 9. 短信服务模块（SMS Service）
- **RocketMQ消费者**：异步消费短信发送消息
- **阿里云短信集成**：支持验证码、通知、面试通知等模板
- **消息格式**：
  - 验证码：`phone|code`
  - 通知：`phone|notification|message`
  - 面试通知：`phone|interview|candidateName|interviewTime|location`
- **REST API**：提供手动触发短信发送的测试接口

### 10. 数据分析模块（Analytics Service）
- **ECharts数据API**：为前端提供各种图表数据
- **支持的图表类型**：
  - 饼图（pie）：学生就业统计、求职状态分布、学历分布
  - 柱状图（bar）：行业分布、城市职位排行、热门技能
  - 折线图（line）：申请趋势分析
  - 漏斗图（funnel）：申请转化率
  - 雷达图（radar）：简历完整度分析
  - 仪表盘（gauge）：实时数据统计
- **大屏展示**：综合数据看板

## 数据库设计

主要数据表：
- `user`: 用户表
- `student`: 学生表
- `enterprise`: 企业表
- `school`: 学校表
- `job`: 职位表
- `resume`: 简历表
- `job_application`: 职位申请表
- `interview`: 面试表
- `notification`: 通知消息表
- `sms_verification`: 短信验证码表

详见：`database/init.sql`

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- RocketMQ 4.9+
- Nacos 2.2+

### 启动步骤

1. **启动基础设施**
   ```bash
   # 启动MySQL
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE recruitment_platform;
   USE recruitment_platform;
   SOURCE database/init.sql;
   
   # 启动Redis
   redis-server
   
   # 启动RocketMQ
   mqnamesrv
   mqbroker -n localhost:9876
   
   # 启动Nacos
   startup.cmd -m standalone
   ```

2. **配置环境变量**
   ```bash
   export OPENAI_API_KEY=your-openai-api-key
   export ALIYUN_ACCESS_KEY_ID=your-access-key-id
   export ALIYUN_ACCESS_KEY_SECRET=your-access-key-secret
   ```

3. **编译项目**
   ```bash
   cd D:/recruitment-platform
   mvn clean install
   ```

4. **启动微服务**（按顺序启动）
   ```bash
   # 启动网关服务
   cd gateway-service
   mvn spring-boot:run
   
   # 启动认证服务
   cd auth-service
   mvn spring-boot:run
   
   # 启动其他服务...
   ```

5. **访问服务**
   - 网关：http://localhost:8080
   - Nacos控制台：http://localhost:8848/nacos

## API示例

### 1. 用户登录
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 2. 发布职位
```bash
POST http://localhost:8080/api/job/jobs/publish
Authorization: Bearer {token}
Content-Type: application/json

{
  "enterpriseId": 1,
  "jobTitle": "Java开发工程师",
  "jobType": 1,
  "salaryMin": 15000,
  "salaryMax": 25000,
  "city": "北京",
  "experienceRequirement": "3-5年",
  "educationRequirement": "本科",
  "jobDescription": "负责后端开发...",
  "status": 1
}
```

### 3. 搜索职位
```bash
GET http://localhost:8080/api/job/jobs/search?keyword=Java&city=北京&salaryMin=10000
```

### 4. 获取智能推荐
```bash
POST http://localhost:8080/api/recommendation/jobs
Content-Type: application/json

{
  "studentProfile": "计算机科学专业，熟悉Java、Spring Boot...",
  "availableJobs": "[职位列表...]"
}
```

### 5. 发送短信验证码（通过RocketMQ）
```bash
# 通过notification-service发送到RocketMQ
POST http://localhost:8080/api/notification/sms/verification-code
Content-Type: application/json

{
  "phone": "13800138000"
}
```

### 6. 获取数据分析图表（ECharts）
```bash
# 获取职位统计（饼图）
GET http://localhost:8080/api/analytics/jobs/statistics?groupBy=category

# 获取申请趋势（折线图）
GET http://localhost:8080/api/analytics/applications/trend?days=7

# 获取学生求职状态分布（饼图）
GET http://localhost:8080/api/analytics/students/job-status

# 获取城市职位排行（柱状图）
GET http://localhost:8080/api/analytics/jobs/city-ranking?limit=10

# 获取综合数据大屏
GET http://localhost:8080/api/analytics/dashboard/overview
```

## SpringAI配置

在`recommendation-service`和`job-service`的`application.yml`中配置：

```yaml
spring:
  ai:
    openai:
      base-url: https://api.openai.com/v1
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-3.5-turbo
          temperature: 0.7
```

## RocketMQ配置

发送短信消息格式：
- 验证码：`phone|code`
- 通知：`phone|notification|message`

## 短信服务配置

在`notification-service`的`application.yml`中配置：

```yaml
aliyun:
  sms:
    access-key-id: ${ALIYUN_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_ACCESS_KEY_SECRET}
    sign-name: 招聘平台
    template-code: SMS_123456789
```

## 注意事项

1. **JWT Secret**: 生产环境请修改`jwt.secret`配置
2. **数据库连接**: 修改各服务的数据库连接配置
3. **Redis数据库**: 不同服务使用不同的Redis数据库（0-4）
4. **Nacos命名空间**: 创建名为`recruitment-platform`的命名空间
5. **API Key安全**: 不要将API Key提交到代码仓库

## 后续开发计划

- [ ] 完善所有Service实现
- [ ] 添加单元测试
- [ ] 集成Swagger/OpenAPI文档
- [ ] 添加日志收集和监控
- [ ] 实现分布式事务（Seata）
- [ ] 添加数据权限控制
- [ ] 实现实时通知（WebSocket）
- [ ] 添加管理后台功能

## 联系方式

如有问题，请提交Issue或联系开发团队。
