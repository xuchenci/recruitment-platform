package com.recruitment.analytics.controller;

import com.recruitment.analytics.service.AnalyticsService;
import com.recruitment.analytics.vo.*;
import com.recruitment.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 数据分析控制器
 * 为前端 ECharts 提供各种图表数据
 */
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    /**
     * 获取职位统计（饼图/柱状图）
     * ECharts 类型：pie, bar
     */
    @GetMapping("/jobs/statistics")
    public Result<?> getJobStatistics(
            @RequestParam(defaultValue = "category") String groupBy,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(analyticsService.getJobStatistics(groupBy, startDate, endDate));
    }
    
    /**
     * 获取申请趋势（折线图）
     * ECharts 类型：line
     */
    @GetMapping("/applications/trend")
    public Result<?> getApplicationTrend(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return Result.success(analyticsService.getApplicationTrend(days, startDate, endDate));
    }
    
    /**
     * 获取学生就业统计（饼图）
     * ECharts 类型：pie
     */
    @GetMapping("/students/employment-statistics")
    public Result<?> getStudentEmploymentStatistics() {
        return Result.success(analyticsService.getStudentEmploymentStatistics());
    }
    
    /**
     * 获取企业行业分布（柱状图/饼图）
     * ECharts 类型：bar, pie
     */
    @GetMapping("/enterprises/industry-distribution")
    public Result<?> getEnterpriseIndustryDistribution() {
        return Result.success(analyticsService.getEnterpriseIndustryDistribution());
    }
    
    /**
     * 获取薪资分布（直方图/柱状图）
     * ECharts 类型：histogram, bar
     */
    @GetMapping("/jobs/salary-distribution")
    public Result<?> getSalaryDistribution() {
        return Result.success(analyticsService.getSalaryDistribution());
    }
    
    /**
     * 获取城市职位数量排行（柱状图/横向柱状图）
     * ECharts 类型：bar, horizontal-bar
     */
    @GetMapping("/jobs/city-ranking")
    public Result<?> getJobCityRanking(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(analyticsService.getJobCityRanking(limit));
    }
    
    /**
     * 获取热门技能标签（词云/柱状图）
     * ECharts 类型：word-cloud, bar
     */
    @GetMapping("/skills/hot-skills")
    public Result<?> getHotSkills(@RequestParam(defaultValue = "20") int limit) {
        return Result.success(analyticsService.getHotSkills(limit));
    }
    
    /**
     * 获取学生求职状态分布（饼图）
     * ECharts 类型：pie
     */
    @GetMapping("/students/job-status")
    public Result<?> getStudentJobStatus() {
        return Result.success(analyticsService.getStudentJobStatus());
    }
    
    /**
     * 获取申请转化率漏斗（漏斗图）
     * ECharts 类型：funnel
     */
    @GetMapping("/applications/conversion-funnel")
    public Result<?> getApplicationConversionFunnel() {
        return Result.success(analyticsService.getApplicationConversionFunnel());
    }
    
    /**
     * 获取企业招聘活跃度排行（柱状图）
     * ECharts 类型：bar
     */
    @GetMapping("/enterprises/recruitment-ranking")
    public Result<?> getEnterpriseRecruitmentRanking(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(analyticsService.getEnterpriseRecruitmentRanking(limit));
    }
    
    /**
     * 获取学生学历分布（饼图）
     * ECharts 类型：pie
     */
    @GetMapping("/students/education-distribution")
    public Result<?> getStudentEducationDistribution() {
        return Result.success(analyticsService.getStudentEducationDistribution());
    }
    
    /**
     * 获取简历完整度分布（雷达图）
     * ECharts 类型：radar
     */
    @GetMapping("/resumes/completeness-radar")
    public Result<?> getResumeCompletenessRadar() {
        return Result.success(analyticsService.getResumeCompletenessRadar());
    }
    
    /**
     * 获取实时数据统计（仪表盘）
     * ECharts 类型：gauge
     */
    @GetMapping("/dashboard/realtime")
    public Result<?> getRealtimeDashboard() {
        return Result.success(analyticsService.getRealtimeDashboard());
    }
    
    /**
     * 获取综合数据统计（用于大屏展示）
     */
    @GetMapping("/dashboard/overview")
    public Result<?> getDashboardOverview() {
        return Result.success(analyticsService.getDashboardOverview());
    }
}