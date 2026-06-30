﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE job_application;
TRUNCATE TABLE job_favorite;
TRUNCATE TABLE job;
TRUNCATE TABLE enterprise;
TRUNCATE TABLE user;
TRUNCATE TABLE student;
SET FOREIGN_KEY_CHECKS=1;

SET @now = NOW();

INSERT INTO user (username, password, phone, email, real_name, user_type, status, created_at, updated_at) VALUES
('enterprise1', '123456', '13800138001', 'contact@tencent.com', '马化腾', 2, 1, @now, @now),
('enterprise2', '123456', '13800138002', 'contact@alibaba.com', '张勇', 2, 1, @now, @now),
('enterprise3', '123456', '13800138003', 'contact@bytedance.com', '张一鸣', 2, 1, @now, @now),
('enterprise4', '123456', '13800138004', 'contact@huawei.com', '任正非', 2, 1, @now, @now),
('enterprise5', '123456', '13800138005', 'contact@meituan.com', '王兴', 2, 1, @now, @now),
('enterprise6', '123456', '13800138006', 'contact@jd.com', '刘强东', 2, 1, @now, @now),
('enterprise7', '123456', '13800138007', 'contact@xiaomi.com', '雷军', 2, 1, @now, @now),
('enterprise8', '123456', '13800138008', 'contact@lenovo.com', '杨元庆', 2, 1, @now, @now),
('enterprise9', '123456', '13800138009', 'contact@didi.com', '程维', 2, 1, @now, @now),
('enterprise10', '123456', '13800138010', 'contact@netease.com', '丁磊', 2, 1, @now, @now),
('student1', '123456', '13900139001', 'student1@test.com', '张同学', 3, 1, @now, @now);

INSERT INTO enterprise (user_id, company_name, company_size, industry, address, website, contact_name, contact_phone, contact_email, description, logo, created_at, updated_at) VALUES
(1, '腾讯', '10000人以上', '互联网', '深圳市南山区科技园南区', 'https://www.tencent.com', '马化腾', '13800138001', 'contact@tencent.com', '腾讯是中国领先的互联网科技公司，提供社交、游戏、金融等多元化服务。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tech%20company%20logo%20blue%20modern%20minimal&image_size=square', @now, @now),
(2, '阿里巴巴', '10000人以上', '电子商务', '杭州市西湖区文三路电子商务大厦', 'https://www.alibaba.com', '张勇', '13800138002', 'contact@alibaba.com', '阿里巴巴是全球领先的电子商务平台，业务涵盖电商、云计算、数字媒体等领域。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ecommerce%20company%20logo%20orange%20modern%20minimal&image_size=square', @now, @now),
(3, '字节跳动', '10000人以上', '互联网', '北京市海淀区中关村科技园8号楼', 'https://www.bytedance.com', '张一鸣', '13800138003', 'contact@bytedance.com', '字节跳动是一家全球领先的科技公司，旗下产品包括抖音、今日头条等。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tech%20company%20logo%20red%20modern%20minimal&image_size=square', @now, @now),
(4, '华为', '10000人以上', '通信设备', '深圳市南山区科技园南区', 'https://www.huawei.com', '任正非', '13800138004', 'contact@huawei.com', '华为是全球领先的ICT基础设施和智能终端提供商。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=telecom%20company%20logo%20red%20sun%20modern&image_size=square', @now, @now),
(5, '美团', '10000人以上', '生活服务', '北京市海淀区中关村科技园', 'https://www.meituan.com', '王兴', '13800138005', 'contact@meituan.com', '美团是中国领先的生活服务平台，提供外卖、餐饮、旅游等服务。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=food%20delivery%20company%20logo%20yellow%20modern&image_size=square', @now, @now),
(6, '京东', '10000人以上', '电子商务', '北京市大兴区亦庄经济开发区', 'https://www.jd.com', '刘强东', '13800138006', 'contact@jd.com', '京东是中国领先的自营式电商平台。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ecommerce%20company%20logo%20red%20modern%20box&image_size=square', @now, @now),
(7, '小米', '1000-9999人', '互联网', '北京市海淀区中关村科技园', 'https://www.mi.com', '雷军', '13800138007', 'contact@xiaomi.com', '小米是一家以智能手机、IoT设备为核心的互联网公司。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartphone%20company%20logo%20orange%20modern%20minimal&image_size=square', @now, @now),
(8, '联想', '1000-9999人', '电子设备', '北京市海淀区中关村科技园', 'https://www.lenovo.com', '杨元庆', '13800138008', 'contact@lenovo.com', '联想是全球领先的个人电脑制造商。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=computer%20company%20logo%20blue%20modern%20tech&image_size=square', @now, @now),
(9, '滴滴出行', '1000-9999人', '出行服务', '北京市海淀区中关村科技园', 'https://www.didiglobal.com', '程维', '13800138009', 'contact@didi.com', '滴滴出行是全球领先的一站式出行平台。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=ride%20sharing%20company%20logo%20orange%20modern%20car&image_size=square', @now, @now),
(10, '网易', '1000-9999人', '游戏', '杭州市滨江区网商路', 'https://www.163.com', '丁磊', '13800138010', 'contact@netease.com', '网易是中国领先的互联网技术公司，业务涵盖游戏、邮箱、音乐等领域。', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=game%20company%20logo%20purple%20modern%20fantasy&image_size=square', @now, @now);

SET @skills1 = '["Java", "Spring Boot", "MySQL", "Redis", "Git"]';
SET @skills2 = '["Vue.js", "React", "TypeScript", "Webpack", "CSS3"]';
SET @skills3 = '["产品设计", "需求分析", "原型设计", "数据分析", "用户研究"]';
SET @skills4 = '["Photoshop", "Sketch", "Figma", "交互设计", "UI设计"]';
SET @skills5 = '["Python", "自动化测试", "JUnit", "Selenium", "性能测试"]';
SET @skills6 = '["SQL", "Python", "数据分析", "Excel", "可视化"]';
SET @skills7 = '["运营策划", "用户增长", "活动运营", "内容运营", "数据分析"]';
SET @skills8 = '["招聘", "培训", "绩效管理", "员工关系", "HR系统"]';
SET @skills9 = '["市场调研", "品牌推广", "广告投放", "活动策划", "文案写作"]';
SET @skills10 = '["财务分析", "预算管理", "报表编制", "税务申报", "审计"]';

SET @benefits1 = '["五险一金", "年终奖", "带薪年假", "餐补", "交通补贴"]';
SET @benefits2 = '["五险一金", "股票期权", "年终双薪", "补充公积金", "年度体检"]';
SET @benefits3 = '["五险一金", "绩效奖金", "节日福利", "培训机会", "团建活动"]';
SET @benefits4 = '["五险一金", "项目奖金", "弹性工作", "远程办公", "员工宿舍"]';
SET @benefits5 = '["五险一金", "年终奖金", "带薪病假", "商业保险", "生日福利"]';
SET @benefits6 = '["五险一金", "季度奖金", "年度旅游", "健身房", "下午茶"]';

SET @desc1 = '负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。';
SET @desc2 = '负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。';
SET @desc3 = '进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。';
SET @desc4 = '设计产品界面和用户体验，制作高质量的UI设计稿。与产品经理和开发团队协作，确保设计方案的实施。持续关注行业趋势，提升设计水平。';
SET @desc5 = '编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。';
SET @desc6 = '分析业务数据，提供数据驱动的决策支持。构建数据分析模型，挖掘数据价值。制作数据可视化报表，向管理层汇报。';
SET @desc7 = '负责用户运营和增长策略，提升用户活跃度和留存率。策划和执行运营活动，推动业务目标达成。分析运营数据，优化运营策略。';
SET @desc8 = '负责公司招聘、培训、绩效管理等人力资源工作。建立和完善HR制度，提升员工满意度。处理员工关系和劳动纠纷。';
SET @desc9 = '制定市场推广策略，提升品牌知名度和影响力。策划和执行市场活动，拓展市场渠道。分析市场数据，优化营销策略。';
SET @desc10 = '负责公司财务报表编制和财务分析。制定预算和成本控制方案。处理税务申报和审计工作。';

SET @req1 = '本科及以上学历，计算机相关专业。扎实的Java编程基础，熟悉Spring Boot框架。熟悉MySQL、Redis等数据库技术。良好的沟通能力和团队协作精神。';
SET @req2 = '本科及以上学历，计算机相关专业。熟练掌握Vue.js或React框架。熟悉TypeScript、Webpack等前端技术。优秀的UI设计审美能力。';
SET @req3 = '本科及以上学历，产品管理、市场营销等相关专业。优秀的需求分析和产品规划能力。熟练使用Axure、Figma等工具。良好的沟通和协调能力。';
SET @req4 = '本科及以上学历，设计相关专业。熟练使用Photoshop、Sketch、Figma等设计工具。优秀的UI设计和交互设计能力。良好的团队协作精神。';
SET @req5 = '本科及以上学历，计算机相关专业。熟悉软件测试流程和方法。熟练使用JUnit、Selenium等测试工具。细心、耐心，具有良好的质量意识。';
SET @req6 = '本科及以上学历，统计学、数学等相关专业。熟练使用SQL、Python等数据分析工具。良好的数据可视化能力。优秀的逻辑思维能力。';
SET @req7 = '本科及以上学历，市场营销、广告等相关专业。优秀的活动策划和执行能力。良好的数据分析和文案写作能力。积极主动，有责任心。';
SET @req8 = '本科及以上学历，人力资源管理相关专业。熟悉招聘、培训、绩效管理流程。良好的沟通和协调能力。熟练使用HR系统。';
SET @req9 = '本科及以上学历，市场营销相关专业。优秀的市场分析和策划能力。良好的沟通和谈判能力。熟悉广告投放和品牌推广。';
SET @req10 = '本科及以上学历，财务、会计相关专业。熟练使用财务软件和Excel。良好的财务分析能力。具备会计从业资格证优先。';

-- 腾讯职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(1, 'Java开发工程师', 1, '深圳', 45, 60, '5-10年', '本科', @desc1, @req1, @skills1, @benefits1, '深圳市南山区科技园南区', 425, 35, 1, @now, @now, @now),
(1, 'Java开发工程师', 1, '深圳', 30, 45, '3-5年', '本科', @desc1, @req1, @skills1, @benefits1, '深圳市南山区科技园南区', 580, 48, 1, @now, @now, @now),
(1, 'Java开发工程师', 1, '深圳', 15, 25, '1-3年', '本科', @desc1, @req1, @skills1, @benefits2, '深圳市南山区科技园南区', 720, 65, 1, @now, @now, @now),
(1, '前端开发工程师', 1, '深圳', 40, 55, '5-10年', '本科', @desc2, @req2, @skills2, @benefits1, '深圳市南山区科技园南区', 380, 30, 1, @now, @now, @now),
(1, '前端开发工程师', 1, '深圳', 25, 38, '3-5年', '本科', @desc2, @req2, @skills2, @benefits1, '深圳市南山区科技园南区', 520, 42, 1, @now, @now, @now),
(1, '产品经理', 1, '深圳', 35, 50, '3-5年', '本科', @desc3, @req3, @skills3, @benefits2, '深圳市南山区科技园南区', 450, 38, 1, @now, @now, @now);

-- 阿里巴巴职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(2, 'Java开发工程师', 1, '杭州', 50, 70, '5-10年', '本科', @desc1, @req1, @skills1, @benefits2, '杭州市西湖区文三路电子商务大厦', 550, 42, 1, @now, @now, @now),
(2, 'Java开发工程师', 1, '杭州', 32, 50, '3-5年', '本科', @desc1, @req1, @skills1, @benefits2, '杭州市西湖区文三路电子商务大厦', 680, 55, 1, @now, @now, @now),
(2, '前端开发工程师', 1, '杭州', 42, 58, '5-10年', '本科', @desc2, @req2, @skills2, @benefits2, '杭州市西湖区文三路电子商务大厦', 420, 35, 1, @now, @now, @now),
(2, '数据分析工程师', 1, '杭州', 38, 52, '3-5年', '本科', @desc6, @req6, @skills6, @benefits3, '杭州市西湖区文三路电子商务大厦', 480, 40, 1, @now, @now, @now),
(2, '产品经理', 1, '杭州', 40, 55, '3-5年', '本科', @desc3, @req3, @skills3, @benefits2, '杭州市西湖区文三路电子商务大厦', 520, 45, 1, @now, @now, @now),
(2, '运营专员', 1, '杭州', 12, 18, '1-3年', '本科', @desc7, @req7, @skills7, @benefits3, '杭州市西湖区文三路电子商务大厦', 650, 58, 1, @now, @now, @now);

-- 字节跳动职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(3, 'Java开发工程师', 1, '北京', 48, 68, '5-10年', '本科', @desc1, @req1, @skills1, @benefits4, '北京市海淀区中关村科技园8号楼', 520, 40, 1, @now, @now, @now),
(3, '前端开发工程师', 1, '北京', 45, 62, '5-10年', '本科', @desc2, @req2, @skills2, @benefits4, '北京市海淀区中关村科技园8号楼', 480, 38, 1, @now, @now, @now),
(3, '产品经理', 1, '北京', 38, 55, '3-5年', '本科', @desc3, @req3, @skills3, @benefits4, '北京市海淀区中关村科技园8号楼', 460, 42, 1, @now, @now, @now),
(3, 'UI设计师', 1, '北京', 32, 48, '3-5年', '本科', @desc4, @req4, @skills4, @benefits5, '北京市海淀区中关村科技园8号楼', 380, 32, 1, @now, @now, @now),
(3, '测试工程师', 1, '北京', 25, 38, '3-5年', '本科', @desc5, @req5, @skills5, @benefits5, '北京市海淀区中关村科技园8号楼', 420, 35, 1, @now, @now, @now),
(3, '数据分析工程师', 1, '北京', 35, 50, '3-5年', '本科', @desc6, @req6, @skills6, @benefits4, '北京市海淀区中关村科技园8号楼', 450, 38, 1, @now, @now, @now);

-- 华为职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(4, 'Java开发工程师', 1, '深圳', 55, 80, '5-10年', '本科', @desc1, @req1, @skills1, @benefits1, '深圳市南山区科技园南区', 620, 48, 1, @now, @now, @now),
(4, 'Java开发工程师', 1, '深圳', 35, 55, '3-5年', '本科', @desc1, @req1, @skills1, @benefits1, '深圳市南山区科技园南区', 750, 62, 1, @now, @now, @now),
(4, '通信工程师', 1, '深圳', 45, 65, '5-10年', '本科', @desc1, @req1, @skills1, @benefits1, '深圳市南山区科技园南区', 480, 35, 1, @now, @now, @now),
(4, '前端开发工程师', 1, '深圳', 42, 58, '3-5年', '本科', @desc2, @req2, @skills2, @benefits2, '深圳市南山区科技园南区', 520, 42, 1, @now, @now, @now),
(4, '测试工程师', 1, '深圳', 28, 42, '3-5年', '本科', @desc5, @req5, @skills5, @benefits3, '深圳市南山区科技园南区', 480, 40, 1, @now, @now, @now),
(4, '产品经理', 1, '深圳', 42, 58, '3-5年', '本科', @desc3, @req3, @skills3, @benefits2, '深圳市南山区科技园南区', 460, 38, 1, @now, @now, @now);

-- 美团职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(5, 'Java开发工程师', 1, '北京', 42, 58, '5-10年', '本科', @desc1, @req1, @skills1, @benefits3, '北京市海淀区中关村科技园', 480, 38, 1, @now, @now, @now),
(5, 'Java开发工程师', 1, '北京', 28, 42, '3-5年', '本科', @desc1, @req1, @skills1, @benefits3, '北京市海淀区中关村科技园', 620, 52, 1, @now, @now, @now),
(5, '前端开发工程师', 1, '北京', 38, 52, '3-5年', '本科', @desc2, @req2, @skills2, @benefits3, '北京市海淀区中关村科技园', 550, 45, 1, @now, @now, @now),
(5, '运营专员', 1, '北京', 15, 22, '1-3年', '本科', @desc7, @req7, @skills7, @benefits4, '北京市海淀区中关村科技园', 780, 68, 1, @now, @now, @now),
(5, '产品经理', 1, '北京', 35, 50, '3-5年', '本科', @desc3, @req3, @skills3, @benefits3, '北京市海淀区中关村科技园', 490, 40, 1, @now, @now, @now),
(5, '数据分析工程师', 1, '北京', 32, 45, '3-5年', '本科', @desc6, @req6, @skills6, @benefits4, '北京市海淀区中关村科技园', 460, 38, 1, @now, @now, @now);

-- 京东职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(6, 'Java开发工程师', 1, '北京', 45, 62, '5-10年', '本科', @desc1, @req1, @skills1, @benefits2, '北京市大兴区亦庄经济开发区', 520, 42, 1, @now, @now, @now),
(6, 'Java开发工程师', 1, '北京', 30, 45, '3-5年', '本科', @desc1, @req1, @skills1, @benefits2, '北京市大兴区亦庄经济开发区', 680, 55, 1, @now, @now, @now),
(6, '前端开发工程师', 1, '北京', 40, 55, '3-5年', '本科', @desc2, @req2, @skills2, @benefits2, '北京市大兴区亦庄经济开发区', 550, 45, 1, @now, @now, @now),
(6, '产品经理', 1, '北京', 38, 52, '3-5年', '本科', @desc3, @req3, @skills3, @benefits3, '北京市大兴区亦庄经济开发区', 480, 40, 1, @now, @now, @now),
(6, '运营专员', 1, '北京', 14, 20, '1-3年', '本科', @desc7, @req7, @skills7, @benefits4, '北京市大兴区亦庄经济开发区', 720, 62, 1, @now, @now, @now),
(6, '测试工程师', 1, '北京', 26, 38, '3-5年', '本科', @desc5, @req5, @skills5, @benefits3, '北京市大兴区亦庄经济开发区', 490, 42, 1, @now, @now, @now);

-- 小米职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(7, 'Java开发工程师', 1, '北京', 38, 55, '5-10年', '本科', @desc1, @req1, @skills1, @benefits5, '北京市海淀区中关村科技园', 450, 35, 1, @now, @now, @now),
(7, 'Java开发工程师', 1, '北京', 25, 38, '3-5年', '本科', @desc1, @req1, @skills1, @benefits5, '北京市海淀区中关村科技园', 580, 48, 1, @now, @now, @now),
(7, '前端开发工程师', 1, '北京', 35, 48, '3-5年', '本科', @desc2, @req2, @skills2, @benefits5, '北京市海淀区中关村科技园', 520, 42, 1, @now, @now, @now),
(7, 'UI设计师', 1, '北京', 28, 42, '3-5年', '本科', @desc4, @req4, @skills4, @benefits6, '北京市海淀区中关村科技园', 420, 35, 1, @now, @now, @now),
(7, '产品经理', 1, '北京', 32, 45, '3-5年', '本科', @desc3, @req3, @skills3, @benefits5, '北京市海淀区中关村科技园', 460, 38, 1, @now, @now, @now),
(7, '测试工程师', 1, '北京', 22, 35, '1-3年', '本科', @desc5, @req5, @skills5, @benefits6, '北京市海淀区中关村科技园', 480, 40, 1, @now, @now, @now);

-- 联想职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(8, 'Java开发工程师', 1, '北京', 35, 50, '5-10年', '本科', @desc1, @req1, @skills1, @benefits3, '北京市海淀区中关村科技园', 420, 32, 1, @now, @now, @now),
(8, 'Java开发工程师', 1, '北京', 22, 35, '3-5年', '本科', @desc1, @req1, @skills1, @benefits3, '北京市海淀区中关村科技园', 550, 45, 1, @now, @now, @now),
(8, '前端开发工程师', 1, '北京', 32, 45, '3-5年', '本科', @desc2, @req2, @skills2, @benefits3, '北京市海淀区中关村科技园', 480, 38, 1, @now, @now, @now),
(8, '产品经理', 1, '北京', 30, 42, '3-5年', '本科', @desc3, @req3, @skills3, @benefits4, '北京市海淀区中关村科技园', 440, 35, 1, @now, @now, @now),
(8, '数据分析工程师', 1, '北京', 28, 40, '3-5年', '本科', @desc6, @req6, @skills6, @benefits4, '北京市海淀区中关村科技园', 460, 38, 1, @now, @now, @now),
(8, '测试工程师', 1, '北京', 20, 32, '1-3年', '本科', @desc5, @req5, @skills5, @benefits5, '北京市海淀区中关村科技园', 450, 38, 1, @now, @now, @now);

-- 滴滴出行职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(9, 'Java开发工程师', 1, '北京', 40, 55, '5-10年', '本科', @desc1, @req1, @skills1, @benefits4, '北京市海淀区中关村科技园', 460, 36, 1, @now, @now, @now),
(9, 'Java开发工程师', 1, '北京', 26, 40, '3-5年', '本科', @desc1, @req1, @skills1, @benefits4, '北京市海淀区中关村科技园', 590, 48, 1, @now, @now, @now),
(9, '前端开发工程师', 1, '北京', 36, 50, '3-5年', '本科', @desc2, @req2, @skills2, @benefits4, '北京市海淀区中关村科技园', 520, 42, 1, @now, @now, @now),
(9, '产品经理', 1, '北京', 33, 46, '3-5年', '本科', @desc3, @req3, @skills3, @benefits5, '北京市海淀区中关村科技园', 450, 38, 1, @now, @now, @now),
(9, '运营专员', 1, '北京', 12, 18, '1-3年', '本科', @desc7, @req7, @skills7, @benefits5, '北京市海淀区中关村科技园', 680, 58, 1, @now, @now, @now),
(9, 'UI设计师', 1, '北京', 25, 38, '1-3年', '本科', @desc4, @req4, @skills4, @benefits6, '北京市海淀区中关村科技园', 420, 35, 1, @now, @now, @now);

-- 网易职位
INSERT INTO job (enterprise_id, job_title, job_type, city, salary_min, salary_max, experience_requirement, education_requirement, job_description, job_responsibility, skill_requirements, benefits, address, view_count, apply_count, status, publish_time, created_at, updated_at) VALUES
(10, 'Java开发工程师', 1, '杭州', 55, 75, '5-10年', '本科', @desc1, @req1, @skills1, @benefits6, '杭州市滨江区网商路', 520, 40, 1, @now, @now, @now),
(10, 'Java开发工程师', 1, '杭州', 32, 48, '3-5年', '本科', @desc1, @req1, @skills1, @benefits6, '杭州市滨江区网商路', 650, 52, 1, @now, @now, @now),
(10, 'Java开发工程师', 1, '杭州', 28, 45, '1-3年', '本科', @desc1, @req1, @skills1, @benefits6, '杭州市滨江区网商路', 720, 58, 1, @now, @now, @now),
(10, '测试工程师', 1, '杭州', 26, 38, '1-3年', '本科', @desc5, @req5, @skills5, @benefits5, '杭州市滨江区网商路', 480, 40, 1, @now, @now, @now),
(10, '运营专员', 1, '杭州', 10, 16, '不限', '大专', @desc7, @req7, @skills7, @benefits5, '杭州市滨江区网商路', 620, 55, 1, @now, @now, @now),
(10, '产品经理', 1, '杭州', 30, 45, '1-3年', '本科', @desc3, @req3, @skills3, @benefits6, '杭州市滨江区网商路', 460, 38, 1, @now, @now, @now);

INSERT INTO student (user_id, college, major, degree, graduation_year, gender, bio, skills, expectation_salary, expectation_position) VALUES
(11, '北京大学', '计算机科学与技术', '本科', '2025', 1, '热爱编程，积极进取，有较强的学习能力和团队协作精神。', '["Java", "Spring Boot", "MySQL", "Redis"]', 15, 'Java开发工程师');

INSERT INTO resume (student_id, resume_name, is_default, is_public, completeness, view_count, download_count, real_name, gender, birth_date, phone, email, city, summary, expected_position, expected_city, expected_salary, educations, experiences, projects, skills, languages, certificates, created_at, updated_at) VALUES
(1, '张同学的简历', 1, 1, 100, 0, 0, '张同学', '男', '2000-01-15', '13900139001', 'student1@test.com', '北京', '热爱编程，积极进取，有较强的学习能力和团队协作精神。', 'Java开发工程师', '北京', '15K', '[{\"school\":\"北京大学\",\"major\":\"计算机科学与技术\",\"degree\":\"本科\",\"startDate\":\"2021-09\",\"endDate\":\"2025-06\"}]', '[{\"company\":\"腾讯\",\"position\":\"Java开发实习生\",\"startDate\":\"2024-06\",\"endDate\":\"2024-09\",\"description\":\"参与微信支付后端开发，负责核心交易流程优化\"}]', '[{\"name\":\"在线商城系统\",\"description\":\"基于Spring Boot开发的电商平台，包含商品管理、订单系统、支付接口\"}]', '[{\"name\":\"Java\",\"level\":\"熟练\"},{\"name\":\"Spring Boot\",\"level\":\"熟练\"},{\"name\":\"MySQL\",\"level\":\"熟练\"},{\"name\":\"Redis\",\"level\":\"熟悉\"}]', '[{\"name\":\"英语\",\"level\":\"CET-6\"}]', '[{\"name\":\"计算机二级\",\"date\":\"2022-03\"}]', @now, @now);

SELECT COUNT(*) as total_jobs FROM job;