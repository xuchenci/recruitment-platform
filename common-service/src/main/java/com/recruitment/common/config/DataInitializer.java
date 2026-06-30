package com.recruitment.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 数据初始化器 - 添加假数据到数据库
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Random random = new Random();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) throws Exception {
        // 先初始化企业用户
        initEnterpriseUsers();
        
        // 再初始化企业数据
        initEnterprises();
        
        // 初始化职位数据
        initJobs();
        
        // 初始化测试学生用户
        initTestStudentUser();

        // 初始化简历数据
        initResumes();
    }

    private void initEnterpriseUsers() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
            if (count != null && count > 0) {
                System.out.println("用户数据已存在，跳过用户初始化");
                return;
            }
        } catch (Exception e) {
            System.out.println("检查表user时出错: " + e.getMessage());
            return;
        }

        String[] enterprisePhones = {"13900139001", "13900139002", "13900139003", "13900139004", "13900139005",
                                     "13900139006", "13900139007", "13900139008", "13900139009", "13900139010"};
        String[] enterpriseNames = {"腾讯科技HR", "阿里巴巴HR", "字节跳动HR", "华为HR", "美团HR",
                                    "京东HR", "网易HR", "小米HR", "滴滴HR", "蚂蚁集团HR"};

        String sql = "INSERT INTO user (username, phone, password, real_name, email, user_type, status, created_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String now = LocalDateTime.now().format(dtf);

        for (int i = 0; i < enterprisePhones.length; i++) {
            jdbcTemplate.update(sql,
                "enterprise" + (i + 1),
                enterprisePhones[i],
                "$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq", // bcrypt加密的123456
                enterpriseNames[i],
                "hr" + (i + 1) + "@example.com",
                2, // 企业用户类型
                1,
                now,
                null // deleted_at
            );
        }

        System.out.println("已初始化 10 条企业用户数据");
    }

    private void initEnterprises() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM enterprise", Integer.class);
            if (count != null && count > 0) {
                System.out.println("企业数据已存在，跳过初始化");
                return;
            }
        } catch (Exception e) {
            System.out.println("检查表enterprise时出错: " + e.getMessage());
            return;
        }

        String[][] enterprises = {
            {"腾讯科技", "10000人以上", "互联网", "深圳市南山区科技园", "https://www.tencent.com", "马化腾", "13800138001", "contact@tencent.com", "腾讯是中国领先的互联网科技公司"},
            {"阿里巴巴集团", "10000人以上", "电子商务", "杭州市余杭区文一西路", "https://www.alibaba.com", "张勇", "13800138002", "contact@alibaba.com", "阿里巴巴是全球领先的电子商务公司"},
            {"字节跳动", "10000人以上", "互联网", "北京市海淀区中关村", "https://www.bytedance.com", "张一鸣", "13800138003", "contact@bytedance.com", "字节跳动是全球领先的科技公司"},
            {"华为技术有限公司", "10000人以上", "通信技术", "深圳市龙岗区坂田", "https://www.huawei.com", "任正非", "13800138004", "contact@huawei.com", "华为是全球领先的ICT基础设施提供商"},
            {"美团", "10000人以上", "本地生活", "北京市朝阳区望京东路", "https://www.meituan.com", "王兴", "13800138005", "contact@meituan.com", "美团是中国领先的本地生活服务平台"},
            {"京东集团", "10000人以上", "电子商务", "北京市大兴区亦庄", "https://www.jd.com", "刘强东", "13800138006", "contact@jd.com", "京东是中国领先的综合电商平台"},
            {"网易", "1000-9999人", "互联网", "杭州市滨江区网商路", "https://www.163.com", "丁磊", "13800138007", "contact@netease.com", "网易是中国领先的互联网技术公司"},
            {"小米科技", "1000-9999人", "智能硬件", "北京市海淀区清河中街", "https://www.mi.com", "雷军", "13800138008", "contact@xiaomi.com", "小米是全球领先的智能硬件公司"},
            {"滴滴出行", "1000-9999人", "出行服务", "北京市海淀区中关村软件园", "https://www.didiglobal.com", "程维", "13800138009", "contact@didi.com", "滴滴是全球领先的一站式出行平台"},
            {"蚂蚁集团", "1000-9999人", "金融科技", "杭州市西湖区西溪路", "https://www.antgroup.com", "井贤栋", "13800138010", "contact@antgroup.com", "蚂蚁集团是全球领先的金融科技开放平台"}
        };

        String sql = "INSERT INTO enterprise (user_id, company_name, company_size, industry, address, website, contact_name, contact_phone, contact_email, description, verified, verify_time, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String now = LocalDateTime.now().format(dtf);

        for (int i = 0; i < enterprises.length; i++) {
            jdbcTemplate.update(sql,
                i + 1, // user_id，对应之前创建的企业用户
                enterprises[i][0],
                enterprises[i][1],
                enterprises[i][2],
                enterprises[i][3],
                enterprises[i][4],
                enterprises[i][5],
                enterprises[i][6],
                enterprises[i][7],
                enterprises[i][8],
                1,
                now,
                now,
                now
            );
        }

        System.out.println("已初始化 10 条企业数据");
    }

    private void initJobs() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM job", Integer.class);
            if (count != null && count > 0) {
                System.out.println("职位数据已存在，跳过初始化");
                return;
            }
        } catch (Exception e) {
            System.out.println("检查表job时出错: " + e.getMessage());
            return;
        }

        // 职位描述模板
        String[] jobDescriptions = {
            "负责公司核心业务系统的设计与开发，参与需求分析、架构设计和技术选型。",
            "与产品团队紧密协作，理解业务需求并转化为技术实现方案。",
            "参与代码审查，确保代码质量和开发规范。",
            "持续优化系统性能，解决技术难题。",
            "负责前端页面的开发与维护，实现高质量的用户界面。",
            "进行产品规划、需求分析和用户研究，推动产品迭代优化。",
            "设计产品原型和用户界面，提升用户体验。",
            "编写测试用例，执行功能测试和性能测试。",
            "分析业务数据，提供数据驱动的决策支持。",
            "负责日常运营工作，提升用户活跃度和留存率。"
        };

        // 岗位要求模板
        String[] jobRequirements = {
            "本科及以上学历，计算机相关专业。",
            "扎实的编程基础，熟悉常用数据结构和算法。",
            "良好的沟通能力和团队协作精神。",
            "有强烈的责任心和学习能力。",
            "能够独立完成任务，有良好的时间管理能力。",
            "有相关项目经验者优先考虑。",
            "熟悉至少一门主流开发语言。",
            "具备良好的英语阅读能力。"
        };

        // 福利待遇选项（JSON字符串格式）
        String[] benefitsOptions = {
            "[\"五险一金\", \"年终奖\", \"带薪年假\", \"餐补\", \"交通补贴\"]",
            "[\"五险一金\", \"股票期权\", \"年终双薪\", \"补充公积金\", \"年度体检\"]",
            "[\"五险一金\", \"绩效奖金\", \"节日福利\", \"培训机会\", \"团建活动\"]",
            "[\"五险一金\", \"项目奖金\", \"弹性工作\", \"远程办公\", \"员工宿舍\"]",
            "[\"五险一金\", \"年终奖金\", \"带薪病假\", \"商业保险\", \"生日福利\"]",
            "[\"五险一金\", \"季度奖金\", \"年度旅游\", \"健身房\", \"下午茶\"]",
            "[\"五险一金\", \"年终奖\", \"育儿假\", \"学历补贴\", \"工龄补贴\"]",
            "[\"五险一金\", \"提成奖金\", \"法定假期\", \"节日礼品\", \"员工食堂\"]"
        };

        // 技能要求选项（JSON字符串格式）
        String[] skillOptions = {
            "[\"Java\", \"Spring Boot\", \"MySQL\", \"Redis\", \"Git\"]",
            "[\"Vue.js\", \"React\", \"TypeScript\", \"Webpack\", \"CSS3\"]",
            "[\"产品设计\", \"需求分析\", \"原型设计\", \"数据分析\", \"用户研究\"]",
            "[\"Photoshop\", \"Sketch\", \"Figma\", \"交互设计\", \"UI设计\"]",
            "[\"Python\", \"自动化测试\", \"JUnit\", \"Selenium\", \"性能测试\"]",
            "[\"SQL\", \"Python\", \"数据分析\", \"Excel\", \"可视化\"]",
            "[\"运营策划\", \"用户增长\", \"活动运营\", \"内容运营\", \"数据分析\"]",
            "[\"招聘\", \"培训\", \"绩效管理\", \"员工关系\", \"HR系统\"]",
            "[\"市场调研\", \"品牌推广\", \"广告投放\", \"活动策划\", \"文案写作\"]",
            "[\"财务分析\", \"预算管理\", \"报表编制\", \"税务申报\", \"审计\"]"
        };

        // 详细工作地点
        String[][] locations = {
            {"北京", "北京市海淀区中关村科技园8号楼"},
            {"上海", "上海市浦东新区张江高科技园区"},
            {"广州", "广州市天河区珠江新城CBD"},
            {"深圳", "深圳市南山区科技园南区"},
            {"杭州", "杭州市西湖区文三路电子商务大厦"},
            {"成都", "成都市高新区天府软件园"},
            {"武汉", "武汉市东湖新技术开发区"},
            {"南京", "南京市雨花台区软件谷"},
            {"西安", "西安市高新区科技二路"},
            {"苏州", "苏州市工业园区金鸡湖大道"}
        };

        String[] jobTitles = {"Java开发工程师", "前端开发工程师", "产品经理", "UI设计师", "测试工程师", "数据分析师", "运营专员", "人力资源专员", "市场营销专员", "财务分析师", "运维工程师", "算法工程师", "后端开发工程师", "全栈开发工程师", "架构师", "项目经理", "技术总监", "高级工程师"};

        String sql = "INSERT INTO job (enterprise_id, job_title, job_type, salary_min, salary_max, city, address, experience_requirement, education_requirement, skill_requirements, job_description, job_responsibility, benefits, status, publish_time, view_count, apply_count, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String now = LocalDateTime.now().format(dtf);

        int jobCount = 0;
        // 每个企业创建6个职位（高级、中级、初级、实习生各不同岗位）
        for (int i = 1; i <= 10; i++) {
            for (int level = 0; level < 6; level++) {
                int idx = (i + level - 1) % jobTitles.length;
                String title = jobTitles[idx];
                int salaryMin, salaryMax;
                String experience;
                String education;
                
                if (level == 0) {
                    title += "（高级/专家）";
                    salaryMin = 30 + random.nextInt(20);
                    salaryMax = salaryMin + 15 + random.nextInt(10);
                    experience = "5-10年";
                    education = "本科及以上";
                } else if (level == 1) {
                    title += "（高级）";
                    salaryMin = 20 + random.nextInt(10);
                    salaryMax = salaryMin + 10 + random.nextInt(5);
                    experience = "3-5年";
                    education = "本科及以上";
                } else if (level == 2) {
                    experience = "1-3年";
                    salaryMin = 12 + random.nextInt(8);
                    salaryMax = salaryMin + 5 + random.nextInt(5);
                    education = "本科";
                } else if (level == 3) {
                    title += "（初级）";
                    experience = "应届/1年";
                    salaryMin = 6 + random.nextInt(4);
                    salaryMax = salaryMin + 3 + random.nextInt(3);
                    education = "本科";
                } else if (level == 4) {
                    title += "（助理）";
                    experience = "不限";
                    salaryMin = 4 + random.nextInt(3);
                    salaryMax = salaryMin + 2 + random.nextInt(2);
                    education = "大专及以上";
                } else {
                    title += "（实习生）";
                    experience = "不限";
                    salaryMin = 2 + random.nextInt(2);
                    salaryMax = salaryMin + 2 + random.nextInt(1);
                    education = "本科在读";
                }

                StringBuilder requirements = new StringBuilder();
                int reqCount = 3 + random.nextInt(3);
                for (int k = 0; k < reqCount; k++) {
                    if (k > 0) requirements.append("\n");
                    requirements.append(jobRequirements[(idx + k) % jobRequirements.length]);
                }

                jdbcTemplate.update(sql,
                    i,
                    title,
                    level == 5 ? 2 : 1,
                    salaryMin,
                    salaryMax,
                    locations[(i - 1) % locations.length][0],
                    locations[(i - 1) % locations.length][1],
                    experience,
                    education,
                    skillOptions[idx % skillOptions.length],
                    jobDescriptions[idx % jobDescriptions.length],
                    requirements.toString(),
                    benefitsOptions[(i + level) % benefitsOptions.length],
                    1,
                    now,
                    random.nextInt(500),
                    random.nextInt(50),
                    now,
                    now
                );
                jobCount++;
            }
        }

        System.out.println("已初始化 " + jobCount + " 条职位数据");
    }

    private void initTestStudentUser() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
            if (count != null && count > 0) {
                System.out.println("用户数据已存在，跳过学生用户初始化");
                return;
            }
        } catch (Exception e) {
            System.out.println("检查表user时出错: " + e.getMessage());
            return;
        }

        String now = LocalDateTime.now().format(dtf);
        
        // 创建学生用户（注意user_id从11开始，因为前面已经有10个企业用户）
        jdbcTemplate.update(
            "INSERT INTO user (username, phone, password, real_name, email, user_type, status, created_at, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            "student1",
            "13800138000",
            "$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq", // bcrypt加密的123456
            "张三",
            "zhangsan@example.com",
            1, // 学生用户类型
            1,
            now,
            null // deleted_at
        );

        // 创建学生信息
        jdbcTemplate.update(
            "INSERT INTO student (user_id, college, major, degree, graduation_year, gender, expectation_salary, skills, bio, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            11, // 第11个用户
            "北京大学",
            "计算机科学",
            "本科",
            2025,
            1,
            15,
            "Java, Python, SQL",
            "热爱编程，积极向上",
            now
        );

        System.out.println("已初始化测试学生用户：13800138000 / 123456");
    }

    private void initResumes() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM resume", Integer.class);
            if (count != null && count > 0) {
                System.out.println("简历数据已存在，跳过初始化");
                return;
            }
        } catch (Exception e) {
            System.out.println("检查表resume时出错: " + e.getMessage());
            return;
        }

        // 获取刚创建的学生的id（student表的user_id=11对应的student.id）
        Long studentId = null;
        try {
            studentId = jdbcTemplate.queryForObject(
                "SELECT id FROM student WHERE user_id = 11", Long.class);
        } catch (Exception e) {
            System.out.println("查询student表时出错: " + e.getMessage());
        }

        if (studentId == null) {
            System.out.println("未找到学生记录，跳过简历初始化");
            return;
        }

        String now = LocalDateTime.now().format(dtf);

        // 为学生创建简历
        String sql = "INSERT INTO resume (student_id, resume_name, is_default, completeness, created_at) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            studentId,
            "张三的简历",
            1, // is_default
            80, // completeness
            now
        );

        jdbcTemplate.update(sql,
            studentId,
            "张三的实习简历",
            0, // is_default
            60, // completeness
            now
        );

        System.out.println("已初始化 2 条简历数据");
    }
}
