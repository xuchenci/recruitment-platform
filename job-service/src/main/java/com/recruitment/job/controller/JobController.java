package com.recruitment.job.controller;

import com.recruitment.common.entity.Job;
import com.recruitment.common.entity.Enterprise;
import com.recruitment.common.mapper.EnterpriseMapper;
import com.recruitment.job.service.JobService;
import com.recruitment.common.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职位控制器
 */
@RestController
@RequestMapping("/jobs")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * 发布职位
     */
    @PostMapping("/publish")
    public Job publishJob(@RequestBody Job job) {
        return jobService.publishJob(job);
    }
    
    /**
     * 获取职位列表（分页 + 多条件筛选）
     */
    @GetMapping("/list")
    public Result<?> getJobList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String salary) {
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Job> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            wrapper.eq("status", 1);

            // 关键词搜索（职位名称或企业名）
            if (keyword != null && !keyword.isEmpty()) {
                wrapper.and(w -> w.like("job_title", keyword).or().like("description", keyword));
            }
            // 城市筛选
            if (city != null && !city.isEmpty()) {
                wrapper.eq("city", city);
            }
            // 职位类别
            if (category != null && !category.isEmpty()) {
                try { wrapper.eq("category_id", Long.parseLong(category)); } catch (NumberFormatException ignored) {}
            }
            // 经验要求（包含选中级别及以下，让用户看全符合条件的机会）
            // 应届生 < 1-3年 < 3-5年 < 5年以上
            if (experience != null && !experience.isEmpty()) {
                List<String> expLevels = new java.util.ArrayList<>();
                switch (experience) {
                    case "fresh": expLevels.add("应届生"); break;
                    case "1-3": expLevels.addAll(List.of("应届生", "1-3年")); break;
                    case "3-5": expLevels.addAll(List.of("应届生", "1-3年", "3-5年")); break;
                    case "5+":  expLevels.addAll(List.of("应届生", "1-3年", "3-5年", "5-10年", "5年以上")); break;
                }
                if (!expLevels.isEmpty()) wrapper.in("experience_requirement", expLevels);
            }
            // 学历要求（包含选中级别及以下：大专 < 本科 < 硕士 < 博士）
            if (education != null && !education.isEmpty()) {
                List<String> eduLevels = new java.util.ArrayList<>();
                switch (education) {
                    case "college": eduLevels.add("大专"); break;
                    case "bachelor": eduLevels.addAll(List.of("大专", "本科")); break;
                    case "master":   eduLevels.addAll(List.of("大专", "本科", "硕士")); break;
                    case "phd":      eduLevels.addAll(List.of("大专", "本科", "硕士", "博士")); break;
                }
                if (!eduLevels.isEmpty()) wrapper.in("education_requirement", eduLevels);
            }
            // 薪资范围
            if (salary != null && !salary.isEmpty()) {
                switch (salary) {
                    case "0-5": wrapper.le("salary_min", 5); break;
                    case "5-10": wrapper.between("salary_min", 5, 10); break;
                    case "10-20": wrapper.between("salary_min", 10, 20); break;
                    case "20-": wrapper.ge("salary_min", 20); break;
                }
            }

            long total = jobService.count(wrapper);
            wrapper.orderByDesc("publish_time");
            int offset = (page - 1) * size;
            wrapper.last("LIMIT " + offset + ", " + size);

            List<Job> jobList = jobService.list(wrapper);

            List<Map<String, Object>> resultList = new java.util.ArrayList<>();
            for (Job job : jobList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", job.getId());
                item.put("title", job.getJobTitle());
                item.put("salaryRange", (job.getSalaryMin() != null && job.getSalaryMax() != null)
                        ? job.getSalaryMin() + "K-" + job.getSalaryMax() + "K" : "面议");
                item.put("location", job.getCity());
                item.put("experience", job.getExperienceRequirement() != null ? job.getExperienceRequirement() : "经验不限");
                item.put("education", job.getEducationRequirement() != null ? job.getEducationRequirement() : "学历不限");
                item.put("viewCount", job.getViewCount() != null ? job.getViewCount() : 0);
                item.put("applicationCount", job.getApplyCount() != null ? job.getApplyCount() : 0);
                item.put("createTime", job.getPublishTime() != null ? job.getPublishTime().toString() : "");

                // 技能标签
                if (job.getSkillRequirements() != null && !job.getSkillRequirements().isEmpty()) {
                    try {
                        item.put("skills", objectMapper.readValue(job.getSkillRequirements(), List.class));
                    } catch (JsonProcessingException e) {
                        item.put("skills", List.of());
                    }
                } else {
                    item.put("skills", List.of());
                }

                // 企业信息
                if (job.getEnterpriseId() != null) {
                    Enterprise enterprise = enterpriseMapper.selectById(job.getEnterpriseId());
                    if (enterprise != null) {
                        item.put("enterpriseName", enterprise.getCompanyName());
                        item.put("enterpriseId", enterprise.getId());
                        item.put("industry", enterprise.getIndustry());
                        item.put("scale", enterprise.getCompanySize());
                        item.put("enterpriseLogo", enterprise.getLogo());
                    }
                }
                if (!item.containsKey("enterpriseName")) {
                    item.put("enterpriseName", "未知企业");
                    item.put("industry", "");
                    item.put("scale", "");
                }

                resultList.add(item);
            }

            int pages = (int) Math.ceil((double) total / size);
            Map<String, Object> pageResult = new HashMap<>();
            pageResult.put("records", resultList);
            pageResult.put("total", total);
            pageResult.put("page", page);
            pageResult.put("size", size);
            pageResult.put("pages", pages);

            return Result.success(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "服务器错误: " + e.getMessage());
        }
    }

    /**
     * 获取职位详情（包含企业信息）
     */
    @GetMapping("/{jobId:[0-9]+}")
    public Result<?> getJob(@PathVariable Long jobId) {
        try {
            jobService.incrementViewCount(jobId);
            Job job = jobService.getById(jobId);
            
            if (job == null) {
                return Result.error(404, "职位不存在");
            }
            
            // 组装返回数据，包含企业信息
            Map<String, Object> result = new HashMap<>();
            result.put("id", job.getId());
            result.put("title", job.getJobTitle());
            result.put("salaryRange", job.getSalaryMin() + "K-" + job.getSalaryMax() + "K");
            result.put("location", job.getCity());
            result.put("experience", job.getExperienceRequirement());
            result.put("education", job.getEducationRequirement());
            result.put("description", job.getJobDescription());
            result.put("requirements", job.getJobResponsibility());
            // 解析JSON格式的benefits字段
            if (job.getBenefits() != null && !job.getBenefits().isEmpty()) {
                try {
                    result.put("benefits", objectMapper.readValue(job.getBenefits(), List.class));
                } catch (JsonProcessingException e) {
                    result.put("benefits", new String[0]);
                }
            } else {
                result.put("benefits", new String[0]);
            }
            result.put("address", job.getAddress());
            result.put("viewCount", job.getViewCount());
            result.put("applicationCount", job.getApplyCount());
            result.put("createTime", job.getPublishTime());
            
            // 获取企业信息
            if (job.getEnterpriseId() != null) {
                Enterprise enterprise = enterpriseMapper.selectById(job.getEnterpriseId());
                if (enterprise != null) {
                    result.put("enterpriseName", enterprise.getCompanyName());
                    result.put("industry", enterprise.getIndustry());
                    result.put("scale", enterprise.getCompanySize());
                    result.put("financingStage", getFinancingStage(enterprise.getCompanySize()));
                    result.put("enterpriseId", enterprise.getId());
                    result.put("enterpriseLogo", enterprise.getLogo());
                }
            }
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "服务器错误: " + e.getMessage());
        }
    }
    
    private String getFinancingStage(String companySize) {
        if (companySize.contains("10000")) return "上市公司";
        if (companySize.contains("1000-9999")) return "C轮及以上";
        if (companySize.contains("100-999")) return "B轮";
        if (companySize.contains("10-99")) return "A轮";
        return "初创";
    }
    
    /**
     * 获取公司主页信息（包含企业信息和发布的职位列表）
     */
    @GetMapping("/enterprise/{enterpriseId}/homepage")
    public Result<?> getEnterpriseHomepage(@PathVariable Long enterpriseId) {
        try {
            Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
            if (enterprise == null) {
                return Result.error(404, "企业不存在");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", enterprise.getId());
            result.put("companyName", enterprise.getCompanyName());
            result.put("companySize", enterprise.getCompanySize());
            result.put("industry", enterprise.getIndustry());
            result.put("address", enterprise.getAddress());
            result.put("website", enterprise.getWebsite());
            result.put("contactName", enterprise.getContactName());
            result.put("contactPhone", enterprise.getContactPhone());
            result.put("contactEmail", enterprise.getContactEmail());
            result.put("description", enterprise.getDescription());
            result.put("logo", enterprise.getLogo());
            result.put("financingStage", getFinancingStage(enterprise.getCompanySize()));
            
            // 获取企业发布的职位列表
            List<Job> jobs = jobService.lambdaQuery()
                    .eq(Job::getEnterpriseId, enterpriseId)
                    .eq(Job::getStatus, 1)
                    .orderByDesc(Job::getPublishTime)
                    .list();
            
            List<Map<String, Object>> jobList = new java.util.ArrayList<>();
            for (Job job : jobs) {
                Map<String, Object> jobItem = new HashMap<>();
                jobItem.put("id", job.getId());
                jobItem.put("title", job.getJobTitle());
                jobItem.put("salaryRange", job.getSalaryMin() + "K-" + job.getSalaryMax() + "K");
                jobItem.put("location", job.getCity());
                jobItem.put("experience", job.getExperienceRequirement());
                jobItem.put("education", job.getEducationRequirement());
                jobItem.put("viewCount", job.getViewCount());
                jobList.add(jobItem);
            }
            
            result.put("jobs", jobList);
            result.put("jobCount", jobs.size());
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "服务器错误: " + e.getMessage());
        }
    }
    
    /**
     * 更新职位
     */
    @PutMapping("/{jobId}")
    public Job updateJob(@PathVariable Long jobId, @RequestBody Job job) {
        return jobService.updateJob(jobId, job);
    }
    
    /**
     * 下架职位
     */
    @DeleteMapping("/{jobId}")
    public String removeJob(@PathVariable Long jobId) {
        boolean result = jobService.removeJob(jobId);
        return result ? "职位已下架" : "下架失败";
    }
    
    /**
     * 搜索职位
     */
    @GetMapping("/search")
    public List<Job> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) Integer salaryMax,
            @RequestParam(required = false) Long categoryId) {
        return jobService.searchJobs(keyword, city, salaryMin, salaryMax, categoryId);
    }
    
    /**
     * 获取企业发布的职位列表
     */
    @GetMapping("/enterprise/{enterpriseId}")
    public List<Job> getJobsByEnterprise(@PathVariable Long enterpriseId) {
        return jobService.lambdaQuery()
                .eq(Job::getEnterpriseId, enterpriseId)
                .list();
    }
    
    /**
     * 获取热门职位（按浏览量排序）
     */
    @GetMapping("/hot")
    public Result<?> getHotJobs(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Job> jobList = jobService.lambdaQuery()
                    .eq(Job::getStatus, 1)
                    .orderByDesc(Job::getViewCount)
                    .last("LIMIT " + limit)
                    .list();

            List<Map<String, Object>> resultList = new java.util.ArrayList<>();
            for (Job job : jobList) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", job.getId());
                item.put("title", job.getJobTitle());
                item.put("salaryRange", job.getSalaryMin() + "K-" + job.getSalaryMax() + "K");
                item.put("location", job.getCity());
                item.put("experience", job.getExperienceRequirement());
                item.put("education", job.getEducationRequirement());
                item.put("viewCount", job.getViewCount());

                if (job.getSkillRequirements() != null && !job.getSkillRequirements().isEmpty()) {
                    try {
                        item.put("skills", objectMapper.readValue(job.getSkillRequirements(), List.class));
                    } catch (JsonProcessingException e) {
                        item.put("skills", new String[0]);
                    }
                } else {
                    item.put("skills", new String[0]);
                }

                if (job.getEnterpriseId() != null) {
                    Enterprise enterprise = enterpriseMapper.selectById(job.getEnterpriseId());
                    if (enterprise != null) {
                        item.put("enterpriseName", enterprise.getCompanyName());
                        item.put("enterpriseId", enterprise.getId());
                        item.put("industry", enterprise.getIndustry());
                        item.put("scale", enterprise.getCompanySize());
                        item.put("enterpriseLogo", enterprise.getLogo());
                    }
                }

                resultList.add(item);
            }

            return Result.success(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "服务器错误: " + e.getMessage());
        }
    }
}
