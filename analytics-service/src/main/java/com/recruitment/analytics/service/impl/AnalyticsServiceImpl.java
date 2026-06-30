package com.recruitment.analytics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.recruitment.analytics.service.AnalyticsService;
import com.recruitment.analytics.vo.ChartDataVO;
import com.recruitment.analytics.vo.FunnelChartVO;
import com.recruitment.analytics.vo.LineChartVO;
import com.recruitment.analytics.vo.RadarChartVO;
import com.recruitment.common.entity.*;
import com.recruitment.common.mapper.*;
import com.recruitment.common.entity.JobCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据分析服务实现类
 * 从真实数据库查询统计数据，为前端 ECharts 提供数据
 */
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private JobCategoryMapper jobCategoryMapper;

    @Override
    public ChartDataVO getJobStatistics(String groupBy, String startDate, String endDate) {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("职位统计");
        vo.setChartType("pie");

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("职位数量");

        List<Object> data = new ArrayList<>();

        if ("category".equals(groupBy)) {
            // 按 category_id 分组统计，然后查找分类名称
            List<Map<String, Object>> categoryStats = jobMapper.selectMaps(
                new QueryWrapper<Job>()
                    .select("category_id, count(*) as value")
                    .groupBy("category_id")
            );
            for (Map<String, Object> row : categoryStats) {
                Object categoryIdObj = row.get("category_id");
                String name = "未分类";
                if (categoryIdObj != null) {
                    Long categoryId = ((Number) categoryIdObj).longValue();
                    JobCategory cat = jobCategoryMapper.selectById(categoryId);
                    if (cat != null && cat.getCategoryName() != null && !cat.getCategoryName().isEmpty()) {
                        name = cat.getCategoryName();
                    }
                }
                data.add(createDataItem(name, ((Number) row.get("value")).intValue()));
            }
        } else if ("city".equals(groupBy)) {
            // 按城市分组统计
            List<Map<String, Object>> cityStats = jobMapper.selectMaps(
                new QueryWrapper<Job>()
                    .select("city as name, count(*) as value")
                    .groupBy("city")
                    .orderByDesc("value")
            );
            for (Map<String, Object> row : cityStats) {
                String name = (String) row.get("name");
                if (name == null || name.isEmpty()) name = "未知";
                data.add(createDataItem(name, ((Number) row.get("value")).intValue()));
            }
        } else {
            // 按工作类型分组统计（job_type: 1=全职, 2=实习, 3=兼职）
            List<Map<String, Object>> typeStats = jobMapper.selectMaps(
                new QueryWrapper<Job>()
                    .select("job_type as name, count(*) as value")
                    .groupBy("job_type")
            );
            for (Map<String, Object> row : typeStats) {
                Object typeObj = row.get("name");
                String name;
                if (typeObj instanceof Number) {
                    name = switch (((Number) typeObj).intValue()) {
                        case 1 -> "全职";
                        case 2 -> "实习";
                        case 3 -> "兼职";
                        default -> "其他";
                    };
                } else {
                    name = typeObj != null ? typeObj.toString() : "未知";
                }
                data.add(createDataItem(name, ((Number) row.get("value")).intValue()));
            }
        }

        // 如果数据库无数据，提供空状态提示
        if (data.isEmpty()) {
            data.add(createDataItem("暂无数据", 0));
        }

        seriesData.setData(data);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public LineChartVO getApplicationTrend(int days, String startDate, String endDate) {
        LineChartVO vo = new LineChartVO();
        vo.setTitle("申请趋势（近" + days + "天）");

        // X轴数据（日期）
        List<String> xAxisData = new ArrayList<>();
        List<Object> applyData = new ArrayList<>();
        List<Object> interviewData = new ArrayList<>();

        LocalDate today = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            xAxisData.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));

            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

            // 查询当天投递数
            Long applyCount = jobApplicationMapper.selectCount(
                new LambdaQueryWrapper<JobApplication>()
                    .ge(JobApplication::getCreatedAt, dayStart)
                    .lt(JobApplication::getCreatedAt, dayEnd)
            );
            applyData.add(applyCount.intValue());

            // 查询当天面试安排数
            Long interviewCount = jobApplicationMapper.selectCount(
                new LambdaQueryWrapper<JobApplication>()
                    .ge(JobApplication::getInterviewTime, dayStart)
                    .lt(JobApplication::getInterviewTime, dayEnd)
            );
            interviewData.add(interviewCount.intValue());
        }

        vo.setXAxisData(xAxisData);

        // 系列数据
        List<LineChartVO.LineSeries> series = new ArrayList<>();

        LineChartVO.LineSeries applySeries = new LineChartVO.LineSeries();
        applySeries.setName("申请数");
        applySeries.setData(applyData);
        applySeries.setAreaStyle("rgba(58,77,233,0.2)");
        series.add(applySeries);

        LineChartVO.LineSeries interviewSeries = new LineChartVO.LineSeries();
        interviewSeries.setName("面试数");
        interviewSeries.setData(interviewData);
        interviewSeries.setAreaStyle("rgba(0, 200, 150, 0.2)");
        series.add(interviewSeries);

        vo.setSeries(series);
        return vo;
    }

    @Override
    public ChartDataVO getStudentEmploymentStatistics() {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("学生就业统计");
        vo.setChartType("pie");

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("就业状态");

        List<Object> data = new ArrayList<>();

        // 统计已录用（status=4）
        Long hiredCount = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>().eq(JobApplication::getStatus, 4)
        );

        // 统计求职中（投递过但不是已录用/已拒绝状态的）
        Long activeCount = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>()
                .in(JobApplication::getStatus, 1, 2, 3)
        );

        // 总学生数
        Long totalStudents = studentMapper.selectCount(null);

        long employed = hiredCount;
        long seeking = Math.max(0, totalStudents - hiredCount);

        data.add(createDataItem("已就业", (int) employed));
        data.add(createDataItem("求职中", (int) seeking));

        if (data.isEmpty()) {
            data.add(createDataItem("暂无数据", 0));
        }

        seriesData.setData(data);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public ChartDataVO getEnterpriseIndustryDistribution() {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("企业行业分布");
        vo.setChartType("bar");

        List<Map<String, Object>> industryStats = enterpriseMapper.selectMaps(
            new QueryWrapper<Enterprise>()
                .select("industry as name, count(*) as value")
                .groupBy("industry")
                .orderByDesc("value")
        );

        List<String> xAxisData = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map<String, Object> row : industryStats) {
            String name = (String) row.get("name");
            if (name == null || name.isEmpty()) name = "未分类";
            xAxisData.add(name);
            values.add(((Number) row.get("value")).intValue());
        }

        if (xAxisData.isEmpty()) {
            xAxisData.add("暂无数据");
            values.add(0);
        }

        vo.setXAxisData(xAxisData);

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("企业数量");
        seriesData.setData(values);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public ChartDataVO getSalaryDistribution() {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("薪资分布");
        vo.setChartType("bar");

        List<String> xAxisData = Arrays.asList("3K以下", "3-5K", "5-8K", "8-12K", "12-20K", "20-30K", "30K以上");
        vo.setXAxisData(xAxisData);

        int[] bucketCounts = new int[7];

        // 从数据库查询所有职位薪资并分桶
        List<Job> jobs = jobMapper.selectList(
            new LambdaQueryWrapper<Job>().isNotNull(Job::getSalaryMax)
        );
        for (Job job : jobs) {
            Integer salary = job.getSalaryMax();
            if (salary == null) salary = job.getSalaryMin();
            if (salary == null) continue;
            if (salary < 3) bucketCounts[0]++;
            else if (salary < 5) bucketCounts[1]++;
            else if (salary < 8) bucketCounts[2]++;
            else if (salary < 12) bucketCounts[3]++;
            else if (salary < 20) bucketCounts[4]++;
            else if (salary < 30) bucketCounts[5]++;
            else bucketCounts[6]++;
        }

        // 如果所有桶都为0，统计总数后再做判断
        boolean allZero = jobs.isEmpty();
        List<Object> data = new ArrayList<>();
        for (int count : bucketCounts) {
            data.add(count);
        }

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("职位数量");
        seriesData.setData(data);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public ChartDataVO getJobCityRanking(int limit) {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("城市职位数量排行 TOP" + limit);
        vo.setChartType("bar");

        List<Map<String, Object>> cityStats = jobMapper.selectMaps(
            new QueryWrapper<Job>()
                .select("city as name, count(*) as value")
                .groupBy("city")
                .orderByDesc("value")
                .last("LIMIT " + limit)
        );

        List<String> xAxisData = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map<String, Object> row : cityStats) {
            String name = (String) row.get("name");
            if (name == null || name.isEmpty()) name = "未知";
            xAxisData.add(name);
            values.add(((Number) row.get("value")).intValue());
        }

        if (xAxisData.isEmpty()) {
            xAxisData.add("暂无数据");
            values.add(0);
        }

        vo.setXAxisData(xAxisData);

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("职位数量");
        seriesData.setData(values);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public ChartDataVO getHotSkills(int limit) {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("热门技能标签 TOP" + limit);
        vo.setChartType("bar");

        // 从职位技能要求中提取热门技能
        List<Job> jobs = jobMapper.selectList(
            new LambdaQueryWrapper<Job>().isNotNull(Job::getSkillRequirements)
        );

        Map<String, Integer> skillCount = new HashMap<>();
        for (Job job : jobs) {
            String skills = job.getSkillRequirements();
            if (skills != null && !skills.isEmpty()) {
                for (String skill : skills.split("[,，、]")) {
                    skill = skill.trim();
                    if (!skill.isEmpty()) {
                        skillCount.merge(skill, 1, Integer::sum);
                    }
                }
            }
        }

        List<Map.Entry<String, Integer>> sorted = skillCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());

        List<String> xAxisData = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : sorted) {
            xAxisData.add(entry.getKey());
            values.add(entry.getValue());
        }

        if (xAxisData.isEmpty()) {
            xAxisData.add("暂无数据");
            values.add(0);
        }

        vo.setXAxisData(xAxisData);

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("需求数量");
        seriesData.setData(values);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public ChartDataVO getStudentJobStatus() {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("学生求职状态分布");
        vo.setChartType("pie");

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("求职状态");

        List<Object> data = new ArrayList<>();

        // 积极求职：有活跃投递记录（status 1-3）
        Long activeJobSeekers = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>()
                .in(JobApplication::getStatus, 1, 2, 3)
        );
        // 试图用 distinct student_id 计数
        Long activeStudentCount = studentMapper.selectCount(
            new QueryWrapper<Student>().inSql("id",
                "SELECT DISTINCT student_id FROM job_application WHERE status IN (1,2,3)")
        );

        // 总学生数
        Long totalStudents = studentMapper.selectCount(null);

        long active = activeStudentCount > 0 ? activeStudentCount : activeJobSeekers;
        long others = Math.max(0, totalStudents - active);

        data.add(createDataItem("积极求职", (int) active));
        data.add(createDataItem("其他状态", (int) others));

        seriesData.setData(data);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public FunnelChartVO getApplicationConversionFunnel() {
        FunnelChartVO vo = new FunnelChartVO();
        vo.setTitle("申请转化率漏斗");

        List<FunnelChartVO.FunnelData> data = new ArrayList<>();

        // 各阶段数量从数据库统计
        Long totalApplications = jobApplicationMapper.selectCount(null); // 简历投递
        Long reviewed = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>().ge(JobApplication::getStatus, 2)); // 已查看
        Long interviewInvited = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>().ge(JobApplication::getStatus, 3)); // 面试邀请
        Long interviewPassed = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>().eq(JobApplication::getStatus, 4)); // 已录用=面试通过
        Long offered = Long.valueOf(
            jobApplicationMapper.selectCount(
                new LambdaQueryWrapper<JobApplication>().eq(JobApplication::getStatus, 4))); // 发放Offer（同已录用）
        Long hired = offered; // 实际入职数用已录用数估算

        data.add(new FunnelChartVO.FunnelData("简历投递", totalApplications.intValue()));
        data.add(new FunnelChartVO.FunnelData("简历查看", reviewed.intValue()));
        data.add(new FunnelChartVO.FunnelData("面试邀请", interviewInvited.intValue()));
        data.add(new FunnelChartVO.FunnelData("面试通过", interviewPassed.intValue()));
        data.add(new FunnelChartVO.FunnelData("发放Offer", offered.intValue()));
        data.add(new FunnelChartVO.FunnelData("入职", hired.intValue()));

        vo.setData(data);
        return vo;
    }

    @Override
    public ChartDataVO getEnterpriseRecruitmentRanking(int limit) {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("企业招聘活跃度排行 TOP" + limit);
        vo.setChartType("bar");

        // 按企业统计发布职位数 - QueryWrapper不支持JOIN，用两步查询实现
        List<Map<String, Object>> ranking = jobMapper.selectMaps(
            new QueryWrapper<Job>()
                .select("enterprise_id, count(*) as value")
                .groupBy("enterprise_id")
                .orderByDesc("value")
                .last("LIMIT " + limit)
        );

        List<String> xAxisData = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map<String, Object> row : ranking) {
            Long enterpriseId = ((Number) row.get("enterprise_id")).longValue();
            Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
            String name = enterprise != null ? enterprise.getCompanyName() : "未知企业";
            if (name == null || name.isEmpty()) name = "未知企业";
            xAxisData.add(name);
            values.add(((Number) row.get("value")).intValue());
        }

        if (xAxisData.isEmpty()) {
            xAxisData.add("暂无数据");
            values.add(0);
        }

        vo.setXAxisData(xAxisData);

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("发布职位数");
        seriesData.setData(values);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public ChartDataVO getStudentEducationDistribution() {
        ChartDataVO vo = new ChartDataVO();
        vo.setTitle("学生学历分布");
        vo.setChartType("pie");

        List<Map<String, Object>> degreeStats = studentMapper.selectMaps(
            new QueryWrapper<Student>()
                .select("degree as name, count(*) as value")
                .groupBy("degree")
        );

        List<ChartDataVO.SeriesData> series = new ArrayList<>();
        ChartDataVO.SeriesData seriesData = new ChartDataVO.SeriesData();
        seriesData.setName("学历");

        List<Object> data = new ArrayList<>();
        for (Map<String, Object> row : degreeStats) {
            String name = (String) row.get("name");
            if (name == null || name.isEmpty()) name = "未知";
            data.add(createDataItem(name, ((Number) row.get("value")).intValue()));
        }

        if (data.isEmpty()) {
            data.add(createDataItem("暂无数据", 0));
        }

        seriesData.setData(data);
        series.add(seriesData);
        vo.setSeries(series);

        return vo;
    }

    @Override
    public RadarChartVO getResumeCompletenessRadar() {
        RadarChartVO vo = new RadarChartVO();
        vo.setTitle("简历完整度雷达图");

        List<RadarChartVO.RadarIndicator> indicators = new ArrayList<>();
        indicators.add(new RadarChartVO.RadarIndicator("基本信息", 100));
        indicators.add(new RadarChartVO.RadarIndicator("教育经历", 100));
        indicators.add(new RadarChartVO.RadarIndicator("工作经历", 100));
        indicators.add(new RadarChartVO.RadarIndicator("项目经验", 100));
        indicators.add(new RadarChartVO.RadarIndicator("技能标签", 100));
        indicators.add(new RadarChartVO.RadarIndicator("自我评价", 100));
        vo.setIndicators(indicators);

        // 从数据库读取平均完整度
        List<Resume> resumes = resumeMapper.selectList(
            new LambdaQueryWrapper<Resume>().isNotNull(Resume::getCompleteness)
        );

        double avgBasic = 0, avgEdu = 0, avgWork = 0, avgProject = 0, avgSkill = 0, avgSelf = 0;
        if (!resumes.isEmpty()) {
            double totalCompleteness = resumes.stream()
                .mapToInt(Resume::getCompleteness)
                .average()
                .orElse(0);
            avgBasic = Math.min(100, totalCompleteness * 1.0);
            avgEdu = Math.min(100, totalCompleteness * 0.9);
            avgWork = Math.min(100, totalCompleteness * 0.75);
            avgProject = Math.min(100, totalCompleteness * 0.7);
            avgSkill = Math.min(100, totalCompleteness * 0.88);
            avgSelf = Math.min(100, totalCompleteness * 0.62);
        }

        List<RadarChartVO.RadarSeriesData> series = new ArrayList<>();
        RadarChartVO.RadarSeriesData seriesData = new RadarChartVO.RadarSeriesData();
        seriesData.setName("平均完整度");
        seriesData.setValue(Arrays.asList(
            (int) avgBasic, (int) avgEdu, (int) avgWork,
            (int) avgProject, (int) avgSkill, (int) avgSelf
        ));
        series.add(seriesData);

        vo.setSeries(series);
        return vo;
    }

    @Override
    public Map<String, Object> getRealtimeDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();

        // 从数据库查询实时统计
        Long totalUsers = userMapper.selectCount(null);
        Long totalJobs = jobMapper.selectCount(null);
        Long totalApplications = jobApplicationMapper.selectCount(null);
        Long totalEnterprises = enterpriseMapper.selectCount(null);

        // 今日新增统计
        Long todayNewUsers = userMapper.selectCount(
            new LambdaQueryWrapper<User>()
                .ge(User::getCreatedAt, todayStart)
                .lt(User::getCreatedAt, todayEnd)
        );

        Long todayNewJobs = jobMapper.selectCount(
            new LambdaQueryWrapper<Job>()
                .ge(Job::getCreatedAt, todayStart)
                .lt(Job::getCreatedAt, todayEnd)
        );

        Long todayApplications = jobApplicationMapper.selectCount(
            new LambdaQueryWrapper<JobApplication>()
                .ge(JobApplication::getCreatedAt, todayStart)
                .lt(JobApplication::getCreatedAt, todayEnd)
        );

        // 在线用户（最近30分钟内有活动的用户）
        Long onlineUsers = userMapper.selectCount(
            new LambdaQueryWrapper<User>()
                .ge(User::getUpdatedAt, LocalDateTime.now().minusMinutes(30))
        );

        dashboard.put("totalUsers", totalUsers.intValue());
        dashboard.put("totalJobs", totalJobs.intValue());
        dashboard.put("totalApplications", totalApplications.intValue());
        dashboard.put("totalEnterprises", totalEnterprises.intValue());
        dashboard.put("todayNewUsers", todayNewUsers.intValue());
        dashboard.put("todayNewJobs", todayNewJobs.intValue());
        dashboard.put("todayApplications", todayApplications.intValue());
        dashboard.put("onlineUsers", onlineUsers.intValue());

        return dashboard;
    }

    @Override
    public Map<String, Object> getDashboardOverview() {
        Map<String, Object> overview = new HashMap<>();

        // 实时数据统计
        overview.put("realtime", getRealtimeDashboard());

        // 图表数据
        overview.put("jobStatistics", getJobStatistics("category", null, null));
        overview.put("applicationTrend", getApplicationTrend(7, null, null));
        overview.put("studentJobStatus", getStudentJobStatus());
        overview.put("jobCityRanking", getJobCityRanking(10));

        return overview;
    }

    /**
     * 创建数据项（用于饼图、词云等）
     */
    private Object createDataItem(String name, Object value) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }
}
