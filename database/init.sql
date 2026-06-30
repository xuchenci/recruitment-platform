-- ============================================
-- 数智化校企招聘与就业生态平台数据库设计
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS recruitment_platform 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE recruitment_platform;

-- ============================================
-- 1. 用户相关表
-- ============================================

-- 用户表（基础用户信息）
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `phone` VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号',
    `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `user_type` TINYINT NOT NULL DEFAULT 1 COMMENT '用户类型：1-学生，2-企业，3-学校，4-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-正常，2-待审核',
    `last_login_time` DATETIME COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted_at` DATETIME COMMENT '删除时间（软删除）',
    INDEX `idx_phone` (`phone`),
    INDEX `idx_user_type` (`user_type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 学生表
CREATE TABLE `student` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID',
    `user_id` BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    `student_no` VARCHAR(50) UNIQUE COMMENT '学号',
    `school_id` BIGINT COMMENT '学校ID',
    `college` VARCHAR(100) COMMENT '学院',
    `major` VARCHAR(100) COMMENT '专业',
    `degree` VARCHAR(50) COMMENT '学历：本科、硕士、博士',
    `enrollment_year` INT COMMENT '入学年份',
    `graduation_year` INT COMMENT '毕业年份',
    `gpa` DECIMAL(3,2) COMMENT 'GPA',
    `gender` TINYINT COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE COMMENT '生日',
    `bio` TEXT COMMENT '个人简介',
    `skills` JSON COMMENT '技能标签',
    `expectation_salary` INT COMMENT '期望薪资（元/月）',
    `expectation_city` VARCHAR(50) COMMENT '期望城市',
    `expectation_industry` VARCHAR(100) COMMENT '期望行业',
    `expectation_position` VARCHAR(100) COMMENT '期望职位',
    `resume_score` INT DEFAULT 0 COMMENT '简历完整度评分',
    `job_status` TINYINT DEFAULT 1 COMMENT '求职状态：1-积极求职，2-观望机会，3-不考虑',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX `idx_school_id` (`school_id`),
    INDEX `idx_major` (`major`),
    INDEX `idx_graduation_year` (`graduation_year`),
    INDEX `idx_job_status` (`job_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 企业表
CREATE TABLE `enterprise` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '企业ID',
    `user_id` BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    `company_name` VARCHAR(100) NOT NULL COMMENT '公司名称',
    `company_size` VARCHAR(50) COMMENT '公司规模',
    `industry` VARCHAR(100) COMMENT '所属行业',
    `business_license` VARCHAR(255) COMMENT '营业执照号',
    `logo` VARCHAR(255) COMMENT '公司LOGO',
    `description` TEXT COMMENT '公司简介',
    `address` VARCHAR(255) COMMENT '公司地址',
    `website` VARCHAR(255) COMMENT '公司官网',
    `contact_name` VARCHAR(50) COMMENT '联系人姓名',
    `contact_phone` VARCHAR(20) COMMENT '联系人电话',
    `contact_email` VARCHAR(100) COMMENT '联系人邮箱',
    `verified` TINYINT DEFAULT 0 COMMENT '是否认证：0-未认证，1-已认证',
    `verify_time` DATETIME COMMENT '认证时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    INDEX `idx_company_name` (`company_name`),
    INDEX `idx_industry` (`industry`),
    INDEX `idx_verified` (`verified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业表';

-- 学校表
CREATE TABLE `school` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学校ID',
    `school_name` VARCHAR(100) NOT NULL COMMENT '学校名称',
    `school_code` VARCHAR(50) UNIQUE COMMENT '学校代码',
    `province` VARCHAR(50) COMMENT '省份',
    `city` VARCHAR(50) COMMENT '城市',
    `address` VARCHAR(255) COMMENT '详细地址',
    `logo` VARCHAR(255) COMMENT '学校LOGO',
    `description` TEXT COMMENT '学校简介',
    `website` VARCHAR(255) COMMENT '学校官网',
    `contact_name` VARCHAR(50) COMMENT '联系人',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `verify_status` TINYINT DEFAULT 0 COMMENT '审核状态：0-未审核，1-已审核',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_school_name` (`school_name`),
    INDEX `idx_city` (`city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学校表';

-- ============================================
-- 2. 职位相关表
-- ============================================

-- 职位分类表
CREATE TABLE `job_category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID（0表示一级分类）',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位分类表';

-- 职位表
CREATE TABLE `job` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '职位ID',
    `enterprise_id` BIGINT NOT NULL COMMENT '企业ID',
    `category_id` BIGINT COMMENT '职位分类ID',
    `job_title` VARCHAR(100) NOT NULL COMMENT '职位名称',
    `job_type` TINYINT COMMENT '工作类型：1-全职，2-实习，3-兼职',
    `salary_min` INT COMMENT '最低薪资（元/月）',
    `salary_max` INT COMMENT '最高薪资（元/月）',
    `city` VARCHAR(50) COMMENT '工作城市',
    `district` VARCHAR(50) COMMENT '工作区域',
    `address` VARCHAR(255) COMMENT '详细地址',
    `experience_requirement` VARCHAR(50) COMMENT '经验要求',
    `education_requirement` VARCHAR(50) COMMENT '学历要求',
    `skill_requirements` JSON COMMENT '技能要求（JSON数组）',
    `job_description` TEXT COMMENT '职位描述',
    `job_responsibility` TEXT COMMENT '工作职责',
    `benefits` JSON COMMENT '福利待遇（JSON数组）',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-招聘中，2-暂停',
    `publish_time` DATETIME COMMENT '发布时间',
    `expire_time` DATETIME COMMENT '过期时间',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `apply_count` INT DEFAULT 0 COMMENT '申请次数',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise`(`id`),
    INDEX `idx_enterprise_id` (`enterprise_id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_city` (`city`),
    INDEX `idx_salary` (`salary_min`, `salary_max`),
    INDEX `idx_status` (`status`),
    INDEX `idx_publish_time` (`publish_time`),
    FULLTEXT INDEX `ft_job_title` (`job_title`, `job_description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- ============================================
-- 3. 简历相关表
-- ============================================

-- 简历表
CREATE TABLE `resume` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '简历ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `resume_name` VARCHAR(100) NOT NULL DEFAULT '我的简历' COMMENT '简历名称',
    `is_default` TINYINT DEFAULT 0 COMMENT '是否默认简历：0-否，1-是',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开：0-否，1-是',
    `completeness` INT DEFAULT 0 COMMENT '完整度（百分比）',
    `view_count` INT DEFAULT 0 COMMENT '被查看次数',
    `download_count` INT DEFAULT 0 COMMENT '被下载次数',
    `file_url` VARCHAR(255) COMMENT '简历文件URL（PDF）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`student_id`) REFERENCES `student`(`id`),
    INDEX `idx_student_id` (`student_id`),
    INDEX `idx_is_default` (`is_default`),
    INDEX `idx_is_public` (`is_public`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历表';

-- 教育经历表
CREATE TABLE `education_experience` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '经历ID',
    `resume_id` BIGINT NOT NULL COMMENT '简历ID',
    `school_name` VARCHAR(100) NOT NULL COMMENT '学校名称',
    `major` VARCHAR(100) NOT NULL COMMENT '专业',
    `degree` VARCHAR(50) NOT NULL COMMENT '学历',
    `start_date` DATE NOT NULL COMMENT '开始时间',
    `end_date` DATE COMMENT '结束时间（为空表示至今）',
    `description` TEXT COMMENT '描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
    INDEX `idx_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教育经历表';

-- 工作经历表
CREATE TABLE `work_experience` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '经历ID',
    `resume_id` BIGINT NOT NULL COMMENT '简历ID',
    `company_name` VARCHAR(100) NOT NULL COMMENT '公司名称',
    `position` VARCHAR(100) NOT NULL COMMENT '职位',
    `start_date` DATE NOT NULL COMMENT '开始时间',
    `end_date` DATE COMMENT '结束时间（为空表示至今）',
    `description` TEXT COMMENT '工作描述',
    `achievement` TEXT COMMENT '工作成就',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
    INDEX `idx_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作经历表';

-- 项目经验表
CREATE TABLE `project_experience` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    `resume_id` BIGINT NOT NULL COMMENT '简历ID',
    `project_name` VARCHAR(100) NOT NULL COMMENT '项目名称',
    `role` VARCHAR(100) COMMENT '担任角色',
    `start_date` DATE NOT NULL COMMENT '开始时间',
    `end_date` DATE COMMENT '结束时间',
    `description` TEXT COMMENT '项目描述',
    `responsibility` TEXT COMMENT '责任描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
    INDEX `idx_resume_id` (`resume_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目经验表';

-- ============================================
-- 4. 申请相关表
-- ============================================

-- 职位申请表
CREATE TABLE `job_application` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    `job_id` BIGINT NOT NULL COMMENT '职位ID',
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `resume_id` BIGINT NOT NULL COMMENT '简历ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1-已投递，2-已查看，3-面试中，4-已录用，5-已拒绝，6-已取消',
    `cover_letter` TEXT COMMENT '求职信',
    `apply_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `interview_time` DATETIME COMMENT '面试时间',
    `interview_location` VARCHAR(255) COMMENT '面试地点',
    `interview_contact` VARCHAR(100) COMMENT '面试联系人',
    `feedback` TEXT COMMENT '反馈意见',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_job_student` (`job_id`, `student_id`),
    FOREIGN KEY (`job_id`) REFERENCES `job`(`id`),
    FOREIGN KEY (`student_id`) REFERENCES `student`(`id`),
    FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`),
    INDEX `idx_job_id` (`job_id`),
    INDEX `idx_student_id` (`student_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_apply_time` (`apply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位申请表';

-- 面试表
CREATE TABLE `interview` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '面试ID',
    `application_id` BIGINT NOT NULL COMMENT '申请ID',
    `interview_type` TINYINT DEFAULT 1 COMMENT '面试类型：1-初试，2-复试，3-终面',
    `interview_mode` TINYINT DEFAULT 1 COMMENT '面试方式：1-现场，2-视频，3-电话',
    `scheduled_time` DATETIME NOT NULL COMMENT '面试时间',
    `duration` INT DEFAULT 60 COMMENT '面试时长（分钟）',
    `location` VARCHAR(255) COMMENT '面试地点/会议链接',
    `interviewer` VARCHAR(100) COMMENT '面试官',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1-待面试，2-已完成，3-已取消',
    `result` TINYINT COMMENT '结果：1-通过，2-未通过，3-待定',
    `feedback` TEXT COMMENT '面试反馈',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`application_id`) REFERENCES `job_application`(`id`),
    INDEX `idx_application_id` (`application_id`),
    INDEX `idx_scheduled_time` (`scheduled_time`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='面试表';

-- ============================================
-- 5. 通知相关表
-- ============================================

-- 通知消息表
CREATE TABLE `notification` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    `receiver_id` BIGINT NOT NULL COMMENT '接收者ID',
    `sender_id` BIGINT COMMENT '发送者ID',
    `type` TINYINT NOT NULL COMMENT '通知类型：1-系统通知，2-申请状态更新，3-面试通知，4-新职位推荐',
    `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
    `content` TEXT COMMENT '通知内容',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `read_time` DATETIME COMMENT '读取时间',
    `related_id` BIGINT COMMENT '关联ID（如申请ID、职位ID等）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_receiver_id` (`receiver_id`),
    INDEX `idx_is_read` (`is_read`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息表';

-- 短信验证码表
CREATE TABLE `sms_verification` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `code` VARCHAR(10) NOT NULL COMMENT '验证码',
    `type` TINYINT NOT NULL COMMENT '类型：1-注册，2-登录，3-重置密码，4-绑定手机',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
    `ip_address` VARCHAR(50) COMMENT 'IP地址',
    `attempts` INT DEFAULT 0 COMMENT '尝试次数',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_phone` (`phone`),
    INDEX `idx_code` (`code`),
    INDEX `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码表';

-- ============================================
-- 6. 系统相关表
-- ============================================

-- 系统配置表
CREATE TABLE `system_config` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_type` VARCHAR(50) COMMENT '配置类型',
    `description` VARCHAR(255) COMMENT '描述',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE `operation_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT COMMENT '用户ID',
    `operation` VARCHAR(100) COMMENT '操作描述',
    `method` VARCHAR(10) COMMENT '请求方法',
    `url` VARCHAR(255) COMMENT '请求URL',
    `ip` VARCHAR(50) COMMENT 'IP地址',
    `user_agent` VARCHAR(500) COMMENT 'User Agent',
    `request_params` JSON COMMENT '请求参数',
    `response_data` JSON COMMENT '响应数据',
    `status` TINYINT DEFAULT 1 COMMENT '状态：1-成功，0-失败',
    `error_message` TEXT COMMENT '错误信息',
    `execution_time` INT COMMENT '执行时间（毫秒）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入管理员用户（密码：admin123）
INSERT INTO `user` (`username`, `password`, `phone`, `email`, `real_name`, `user_type`, `status`) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138000', 'admin@recruitment.com', '系统管理员', 4, 1);

-- 插入职位分类数据
INSERT INTO `job_category` (`category_name`, `parent_id`, `sort_order`) VALUES
('技术类', 0, 1),
('产品类', 0, 2),
('设计类', 0, 3),
('市场类', 0, 4),
('运营类', 0, 5),
('职能类', 0, 6);

INSERT INTO `job_category` (`category_name`, `parent_id`, `sort_order`) VALUES
('Java开发', 1, 1),
('前端开发', 1, 2),
('Python开发', 1, 3),
('数据分析', 1, 4),
('AI/机器学习', 1, 5),
('产品经理', 2, 1),
('UI设计', 3, 1),
('UX设计', 3, 2);

COMMIT;
