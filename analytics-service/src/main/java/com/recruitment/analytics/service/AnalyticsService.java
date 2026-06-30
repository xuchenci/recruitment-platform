package com.recruitment.analytics.service;

import com.recruitment.analytics.vo.ChartDataVO;
import com.recruitment.analytics.vo.FunnelChartVO;
import com.recruitment.analytics.vo.LineChartVO;
import com.recruitment.analytics.vo.RadarChartVO;

import java.util.List;
import java.util.Map;

/**
 * 数据分析服务接口
 */
public interface AnalyticsService {
    
    /**
     * 获取职位统计
     * @param groupBy 分组字段（category, city, type）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 图表数据
     */
    ChartDataVO getJobStatistics(String groupBy, String startDate, String endDate);
    
    /**
     * 获取申请趋势（折线图）
     * @param days 天数
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 折线图数据
     */
    LineChartVO getApplicationTrend(int days, String startDate, String endDate);
    
    /**
     * 获取学生就业统计
     * @return 饼图数据
     */
    ChartDataVO getStudentEmploymentStatistics();
    
    /**
     * 获取企业行业分布
     * @return 饼图/柱状图数据
     */
    ChartDataVO getEnterpriseIndustryDistribution();
    
    /**
     * 获取薪资分布
     * @return 直方图/柱状图数据
     */
    ChartDataVO getSalaryDistribution();
    
    /**
     * 获取城市职位数量排行
     * @param limit 限制数量
     * @return 柱状图数据
     */
    ChartDataVO getJobCityRanking(int limit);
    
    /**
     * 获取热门技能标签
     * @param limit 限制数量
     * @return 词云/柱状图数据
     */
    ChartDataVO getHotSkills(int limit);
    
    /**
     * 获取学生求职状态分布
     * @return 饼图数据
     */
    ChartDataVO getStudentJobStatus();
    
    /**
     * 获取申请转化率漏斗
     * @return 漏斗图数据
     */
    FunnelChartVO getApplicationConversionFunnel();
    
    /**
     * 获取企业招聘活跃度排行
     * @param limit 限制数量
     * @return 柱状图数据
     */
    ChartDataVO getEnterpriseRecruitmentRanking(int limit);
    
    /**
     * 获取学生学历分布
     * @return 饼图数据
     */
    ChartDataVO getStudentEducationDistribution();
    
    /**
     * 获取简历完整度雷达图
     * @return 雷达图数据
     */
    RadarChartVO getResumeCompletenessRadar();
    
    /**
     * 获取实时数据统计（仪表盘）
     * @return 统计数据
     */
    Map<String, Object> getRealtimeDashboard();
    
    /**
     * 获取综合数据统计（大屏展示）
     * @return 综合数据
     */
    Map<String, Object> getDashboardOverview();
}
