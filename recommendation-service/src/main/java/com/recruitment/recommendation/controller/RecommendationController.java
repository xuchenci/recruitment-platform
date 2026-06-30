package com.recruitment.recommendation.controller;

import com.recruitment.recommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 智能推荐控制器
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    /**
     * 获取职业建议
     */
    @PostMapping("/career-advice")
    public String getCareerAdvice(@RequestBody Map<String, String> request) {
        String studentProfile = request.get("studentProfile");
        String preferences = request.get("preferences");
        return recommendationService.generateCareerAdvice(studentProfile, preferences);
    }
    
    /**
     * 推荐匹配的职位
     */
    @PostMapping("/jobs")
    public String recommendJobs(@RequestBody Map<String, String> request) {
        String studentProfile = request.get("studentProfile");
        String availableJobs = request.get("availableJobs");
        return recommendationService.recommendJobs(studentProfile, availableJobs);
    }
    
    /**
     * 优化简历
     */
    @PostMapping("/optimize-resume")
    public String optimizeResume(@RequestBody Map<String, String> request) {
        String resumeContent = request.get("resumeContent");
        String targetJob = request.get("targetJob");
        return recommendationService.optimizeResume(resumeContent, targetJob);
    }
    
    /**
     * 面试准备建议
     */
    @PostMapping("/interview-prep")
    public String interviewPreparation(@RequestBody Map<String, String> request) {
        String jobDescription = request.get("jobDescription");
        String companyInfo = request.get("companyInfo");
        return recommendationService.interviewPreparation(jobDescription, companyInfo);
    }
    
    /**
     * 向量搜索相似职位
     */
    @PostMapping("/similar-jobs")
    public Object findSimilarJobs(@RequestBody Map<String, String> request) {
        String jobDescription = request.get("jobDescription");
        return recommendationService.findSimilarJobs(jobDescription);
    }
    
    /**
     * 索引职位到向量数据库
     */
    @PostMapping("/index-job")
    public String indexJob(@RequestBody Map<String, String> request) {
        String jobId = request.get("jobId");
        String jobDescription = request.get("jobDescription");
        recommendationService.indexJob(jobId, jobDescription);
        return "职位已索引";
    }
}
