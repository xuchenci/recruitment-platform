-- 插入60条职位数据
SET @now = NOW();

-- 技能要求
SET @skills1 = '[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]';
SET @skills2 = '[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]';
SET @skills3 = '[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]';
SET @skills4 = '[\"Photoshop\", \"Sketch\", \"Figma\", \"交互设计\", \"UI设计\"]';
SET @skills5 = '[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]';
SET @skills6 = '[\"SQL\", \"Python\", \"数据分析\", \"Excel\", \"可视化\"]';
SET @skills7 = '[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]';
SET @skills8 = '[\"招聘\", \"培训\", \"绩效管理\", \"员工关系\", \"HR系统\"]';
SET @skills9 = '[\"市场调研\", \"品牌推广\", \"广告投放\", \"活动策划\", \"文案写作\"]';
SET @skills10 = '[\"财务分析\", \"预算管理\", \"报表编制\", \"税务申报\", \"审计\"]';

-- 福利待遇
SET @benefits1 = '[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]';
SET @benefits2 = '[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]';
SET @benefits3 = '[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]';
SET @benefits4 = '[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]';
SET @benefits5 = '[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]';
SET @benefits6 = '[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]';

-- 职位描述
SET @desc1 = '负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。与产品团队紧密协作，理解业务需求并转化为技术实现方案。参与代码审查，确保代码质量和开发规范。';
SET @desc2 = '负责前端页面的开发与维护，实现高质量的用户界面。与设计师协作，将设计稿转化为可交互的网页应用。优化前端性能，提升用户体验。';
SET @desc3 = '进行产品规划、需求分析和用户研究，推动产品迭代优化。设计产品原型和用户界面，提升用户体验。与开发团队协作，确保产品按时交付。';
SET @desc4 = '设计产品界面和用户体验，制作高质量的UI设计稿。与产品经理和开发团队协作，确保设计方案的实施。持续关注行业趋势，提升设计水平。';
SET @desc5 = '编写测试用例，执行功能测试和性能测试。定位和跟踪软件缺陷，确保产品质量。与开发团队协作，优化测试流程。';
SET @desc6 = '分析业务数据，提供数据驱动的决策支持。构建数据分析模型，挖掘数据价值。制作数据可视化报表，向管理层汇报。';
SET @desc7 = '负责日常运营工作，提升用户活跃度和留存率。策划和执行运营活动，推动用户增长。分析运营数据，优化运营策略。';
SET @desc8 = '负责招聘工作，包括职位发布、简历筛选、面试安排。组织员工培训，提升员工能力。管理员工关系，维护良好的企业文化。';
SET @desc9 = '进行市场调研，制定市场推广策略。策划和执行市场营销活动，提升品牌知名度。分析市场数据，优化营销策略。';
SET @desc10 = '负责财务分析和预算管理，编制财务报表。进行税务申报和审计工作。管理公司资金，确保财务安全。';

-- 岗位要求
SET @req1 = '本科及以上学历，计算机相关专业。扎实的编程基础，熟悉常用数据结构和算法。良好的沟通能力和团队协作精神。';
SET @req2 = '本科及以上学历，设计或计算机相关专业。熟练使用设计工具，如Figma、Sketch等。良好的审美能力和用户体验意识。';
SET @req3 = '本科及以上学历，管理、营销或计算机相关专业。有产品规划和需求分析经验。良好的沟通能力和项目管理能力。';
SET @req4 = '本科及以上学历，统计学、数学或计算机相关专业。熟练使用数据分析工具，如Python、SQL等。良好的逻辑思维能力。';
SET @req5 = '本科及以上学历，人力资源管理或相关专业。熟悉招聘流程和员工培训体系。良好的沟通能力和组织协调能力。';

-- 企业1：腾讯
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(1, 'Java开发工程师（高级/专家）', 1, 45, 60, '深圳', '深圳市南山区科技园南区', '5-10年', '本科及以上', @skills1, @desc1, @req1, @benefits2, 1, @now, 420, 35, @now, @now),
(1, '前端开发工程师（高级）', 1, 28, 40, '深圳', '深圳市南山区科技园南区', '3-5年', '本科及以上', @skills2, @desc2, @req1, @benefits1, 1, @now, 380, 28, @now, @now),
(1, '产品经理', 1, 22, 35, '深圳', '深圳市南山区科技园南区', '1-3年', '本科', @skills3, @desc3, @req3, @benefits3, 1, @now, 350, 25, @now, @now),
(1, 'UI设计师（初级）', 1, 8, 12, '深圳', '深圳市南山区科技园南区', '应届/1年', '本科', @skills4, @desc4, @req2, @benefits4, 1, @now, 280, 20, @now, @now),
(1, '测试工程师（助理）', 1, 5, 8, '深圳', '深圳市南山区科技园南区', '不限', '大专及以上', @skills5, @desc5, @req1, @benefits5, 1, @now, 220, 15, @now, @now),
(1, '数据分析师（实习生）', 2, 3, 5, '深圳', '深圳市南山区科技园南区', '不限', '本科在读', @skills6, @desc6, @req4, @benefits6, 1, @now, 180, 12, @now, @now);

-- 企业2：阿里巴巴
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(2, '前端开发工程师（高级/专家）', 1, 48, 65, '杭州', '杭州市西湖区文三路电子商务大厦', '5-10年', '本科及以上', @skills2, @desc2, @req1, @benefits2, 1, @now, 450, 40, @now, @now),
(2, 'Java开发工程师（高级）', 1, 30, 45, '杭州', '杭州市西湖区文三路电子商务大厦', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 410, 32, @now, @now),
(2, '算法工程师', 1, 25, 40, '杭州', '杭州市西湖区文三路电子商务大厦', '1-3年', '本科', @skills6, @desc6, @req4, @benefits3, 1, @now, 380, 28, @now, @now),
(2, '测试工程师（初级）', 1, 9, 13, '杭州', '杭州市西湖区文三路电子商务大厦', '应届/1年', '本科', @skills5, @desc5, @req1, @benefits4, 1, @now, 300, 22, @now, @now),
(2, '运营专员（助理）', 1, 6, 10, '杭州', '杭州市西湖区文三路电子商务大厦', '不限', '大专及以上', @skills7, @desc7, @req3, @benefits5, 1, @now, 250, 18, @now, @now),
(2, 'UI设计师（实习生）', 2, 3, 5, '杭州', '杭州市西湖区文三路电子商务大厦', '不限', '本科在读', @skills4, @desc4, @req2, @benefits6, 1, @now, 200, 14, @now, @now);

-- 企业3：字节跳动
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(3, '算法工程师（高级/专家）', 1, 50, 70, '北京', '北京市海淀区中关村科技园8号楼', '5-10年', '本科及以上', @skills6, @desc6, @req4, @benefits2, 1, @now, 480, 45, @now, @now),
(3, '后端开发工程师（高级）', 1, 32, 48, '北京', '北京市海淀区中关村科技园8号楼', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 430, 35, @now, @now),
(3, '全栈开发工程师', 1, 20, 35, '北京', '北京市海淀区中关村科技园8号楼', '1-3年', '本科', @skills2, @desc2, @req1, @benefits3, 1, @now, 360, 26, @now, @now),
(3, '产品经理（初级）', 1, 7, 12, '北京', '北京市海淀区中关村科技园8号楼', '应届/1年', '本科', @skills3, @desc3, @req3, @benefits4, 1, @now, 290, 21, @now, @now),
(3, '市场营销专员（助理）', 1, 5, 9, '北京', '北京市海淀区中关村科技园8号楼', '不限', '大专及以上', @skills9, @desc9, @req3, @benefits5, 1, @now, 230, 16, @now, @now),
(3, '运营专员（实习生）', 2, 3, 5, '北京', '北京市海淀区中关村科技园8号楼', '不限', '本科在读', @skills7, @desc7, @req3, @benefits6, 1, @now, 190, 13, @now, @now);

-- 企业4：华为
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(4, '架构师（高级/专家）', 1, 55, 80, '深圳', '深圳市南山区科技园南区', '5-10年', '本科及以上', @skills1, @desc1, @req1, @benefits2, 1, @now, 500, 50, @now, @now),
(4, '后端开发工程师（高级）', 1, 35, 50, '深圳', '深圳市南山区科技园南区', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 440, 38, @now, @now),
(4, '运维工程师', 1, 22, 35, '深圳', '深圳市南山区科技园南区', '1-3年', '本科', @skills1, @desc1, @req1, @benefits3, 1, @now, 340, 24, @now, @now),
(4, '数据分析师（初级）', 1, 8, 13, '深圳', '深圳市南山区科技园南区', '应届/1年', '本科', @skills6, @desc6, @req4, @benefits4, 1, @now, 270, 19, @now, @now),
(4, '人力资源专员（助理）', 1, 5, 9, '深圳', '深圳市南山区科技园南区', '不限', '大专及以上', @skills8, @desc8, @req5, @benefits5, 1, @now, 210, 14, @now, @now),
(4, '测试工程师（实习生）', 2, 3, 5, '深圳', '深圳市南山区科技园南区', '不限', '本科在读', @skills5, @desc5, @req1, @benefits6, 1, @now, 170, 11, @now, @now);

-- 企业5：美团
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(5, '后端开发工程师（高级/专家）', 1, 42, 58, '北京', '北京市海淀区中关村科技园8号楼', '5-10年', '本科及以上', @skills1, @desc1, @req1, @benefits2, 1, @now, 430, 36, @now, @now),
(5, '前端开发工程师（高级）', 1, 26, 38, '北京', '北京市海淀区中关村科技园8号楼', '3-5年', '本科及以上', @skills2, @desc2, @req1, @benefits1, 1, @now, 390, 29, @now, @now),
(5, '产品经理', 1, 18, 32, '北京', '北京市海淀区中关村科技园8号楼', '1-3年', '本科', @skills3, @desc3, @req3, @benefits3, 1, @now, 330, 23, @now, @now),
(5, 'UI设计师（初级）', 1, 7, 11, '北京', '北京市海淀区中关村科技园8号楼', '应届/1年', '本科', @skills4, @desc4, @req2, @benefits4, 1, @now, 260, 18, @now, @now),
(5, '运营专员（助理）', 1, 5, 8, '北京', '北京市海淀区中关村科技园8号楼', '不限', '大专及以上', @skills7, @desc7, @req3, @benefits5, 1, @now, 200, 13, @now, @now),
(5, '财务分析师（实习生）', 2, 3, 5, '北京', '北京市海淀区中关村科技园8号楼', '不限', '本科在读', @skills10, @desc10, @req4, @benefits6, 1, @now, 160, 10, @now, @now);

-- 企业6：京东
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(6, '架构师（高级/专家）', 1, 52, 75, '北京', '北京市海淀区中关村科技园8号楼', '5-10年', '本科及以上', @skills1, @desc1, @req1, @benefits2, 1, @now, 470, 42, @now, @now),
(6, 'Java开发工程师（高级）', 1, 30, 45, '北京', '北京市海淀区中关村科技园8号楼', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 400, 31, @now, @now),
(6, '算法工程师', 1, 22, 38, '北京', '北京市海淀区中关村科技园8号楼', '1-3年', '本科', @skills6, @desc6, @req4, @benefits3, 1, @now, 350, 25, @now, @now),
(6, '测试工程师（初级）', 1, 8, 12, '北京', '北京市海淀区中关村科技园8号楼', '应届/1年', '本科', @skills5, @desc5, @req1, @benefits4, 1, @now, 280, 20, @now, @now),
(6, '市场营销专员（助理）', 1, 5, 9, '北京', '北京市海淀区中关村科技园8号楼', '不限', '大专及以上', @skills9, @desc9, @req3, @benefits5, 1, @now, 220, 15, @now, @now),
(6, '数据分析师（实习生）', 2, 3, 5, '北京', '北京市海淀区中关村科技园8号楼', '不限', '本科在读', @skills6, @desc6, @req4, @benefits6, 1, @now, 180, 12, @now, @now);

-- 企业7：网易
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(7, '前端开发工程师（高级/专家）', 1, 40, 55, '杭州', '杭州市西湖区文三路电子商务大厦', '5-10年', '本科及以上', @skills2, @desc2, @req1, @benefits2, 1, @now, 410, 34, @now, @now),
(7, '后端开发工程师（高级）', 1, 28, 42, '杭州', '杭州市西湖区文三路电子商务大厦', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 380, 28, @now, @now),
(7, '产品经理', 1, 18, 30, '杭州', '杭州市西湖区文三路电子商务大厦', '1-3年', '本科', @skills3, @desc3, @req3, @benefits3, 1, @now, 320, 22, @now, @now),
(7, 'UI设计师（初级）', 1, 8, 12, '杭州', '杭州市西湖区文三路电子商务大厦', '应届/1年', '本科', @skills4, @desc4, @req2, @benefits4, 1, @now, 250, 17, @now, @now),
(7, '运营专员（助理）', 1, 5, 9, '杭州', '杭州市西湖区文三路电子商务大厦', '不限', '大专及以上', @skills7, @desc7, @req3, @benefits5, 1, @now, 190, 12, @now, @now),
(7, '测试工程师（实习生）', 2, 3, 5, '杭州', '杭州市西湖区文三路电子商务大厦', '不限', '本科在读', @skills5, @desc5, @req1, @benefits6, 1, @now, 160, 10, @now, @now);

-- 企业8：小米
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(8, '架构师（高级/专家）', 1, 48, 68, '北京', '北京市海淀区中关村科技园8号楼', '5-10年', '本科及以上', @skills1, @desc1, @req1, @benefits2, 1, @now, 460, 40, @now, @now),
(8, 'Java开发工程师（高级）', 1, 28, 42, '北京', '北京市海淀区中关村科技园8号楼', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 390, 30, @now, @now),
(8, '全栈开发工程师', 1, 18, 30, '北京', '北京市海淀区中关村科技园8号楼', '1-3年', '本科', @skills2, @desc2, @req1, @benefits3, 1, @now, 330, 24, @now, @now),
(8, '运维工程师（初级）', 1, 7, 11, '北京', '北京市海淀区中关村科技园8号楼', '应届/1年', '本科', @skills1, @desc1, @req1, @benefits4, 1, @now, 240, 16, @now, @now),
(8, '人力资源专员（助理）', 1, 5, 8, '北京', '北京市海淀区中关村科技园8号楼', '不限', '大专及以上', @skills8, @desc8, @req5, @benefits5, 1, @now, 180, 11, @now, @now),
(8, '算法工程师（实习生）', 2, 3, 5, '北京', '北京市海淀区中关村科技园8号楼', '不限', '本科在读', @skills6, @desc6, @req4, @benefits6, 1, @now, 170, 11, @now, @now);

-- 企业9：滴滴
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(9, '后端开发工程师（高级/专家）', 1, 44, 60, '北京', '北京市海淀区中关村科技园8号楼', '5-10年', '本科及以上', @skills1, @desc1, @req1, @benefits2, 1, @now, 440, 37, @now, @now),
(9, '前端开发工程师（高级）', 1, 26, 38, '北京', '北京市海淀区中关村科技园8号楼', '3-5年', '本科及以上', @skills2, @desc2, @req1, @benefits1, 1, @now, 370, 27, @now, @now),
(9, '产品经理', 1, 18, 32, '北京', '北京市海淀区中关村科技园8号楼', '1-3年', '本科', @skills3, @desc3, @req3, @benefits3, 1, @now, 310, 22, @now, @now),
(9, '数据分析师（初级）', 1, 8, 12, '北京', '北京市海淀区中关村科技园8号楼', '应届/1年', '本科', @skills6, @desc6, @req4, @benefits4, 1, @now, 260, 18, @now, @now),
(9, '财务分析师（助理）', 1, 5, 9, '北京', '北京市海淀区中关村科技园8号楼', '不限', '大专及以上', @skills10, @desc10, @req4, @benefits5, 1, @now, 190, 12, @now, @now),
(9, 'UI设计师（实习生）', 2, 3, 5, '北京', '北京市海淀区中关村科技园8号楼', '不限', '本科在读', @skills4, @desc4, @req2, @benefits6, 1, @now, 160, 10, @now, @now);

-- 企业10：蚂蚁集团
INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES
(10, '算法工程师（高级/专家）', 1, 55, 75, '杭州', '杭州市西湖区文三路电子商务大厦', '5-10年', '本科及以上', @skills6, @desc6, @req4, @benefits2, 1, @now, 520, 52, @now, @now),
(10, '后端开发工程师（高级）', 1, 32, 48, '杭州', '杭州市西湖区文三路电子商务大厦', '3-5年', '本科及以上', @skills1, @desc1, @req1, @benefits1, 1, @now, 450, 39, @now, @now),
(10, '架构师', 1, 28, 45, '杭州', '杭州市西湖区文三路电子商务大厦', '1-3年', '本科', @skills1, @desc1, @req1, @benefits3, 1, @now, 380, 29, @now, @now),
(10, '测试工程师（初级）', 1, 9, 13, '杭州', '杭州市西湖区文三路电子商务大厦', '应届/1年', '本科', @skills5, @desc5, @req1, @benefits4, 1, @now, 290, 21, @now, @now),
(10, '市场营销专员（助理）', 1, 5, 9, '杭州', '杭州市西湖区文三路电子商务大厦', '不限', '大专及以上', @skills9, @desc9, @req3, @benefits5, 1, @now, 210, 14, @now, @now),
(10, '运营专员（实习生）', 2, 3, 5, '杭州', '杭州市西湖区文三路电子商务大厦', '不限', '本科在读', @skills7, @desc7, @req3, @benefits6, 1, @now, 180, 12, @now, @now);

SELECT COUNT(*) as total_jobs FROM job;