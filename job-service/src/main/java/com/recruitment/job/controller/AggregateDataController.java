package com.recruitment.job.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.common.util.HttpApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 聚合数据控制器
 * 集成第三方招聘API获取真实招聘信息
 */
@RestController
@RequestMapping("/aggregate")
public class AggregateDataController {

    private static final Logger log = LoggerFactory.getLogger(AggregateDataController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.external.apijobs.api-key}")
    private String apiJobsKey;

    @Value("${spring.external.apijobs.base-url}")
    private String apiJobsBaseUrl;

    /**
     * 搜索招聘数据 (APIJobs)
     * 
     * @param keyword 搜索关键词
     * @param location 工作地点（可选）
     * @param employmentType 就业类型：FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY
     * @param page 页码
     * @param limit 每页数量
     */
    @GetMapping("/jobs/search")
    public Map<String, Object> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String employmentType,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 构建请求参数
            Map<String, Object> requestBody = new HashMap<>();
            if (keyword != null && !keyword.isEmpty()) {
                requestBody.put("q", keyword);
            }
            if (location != null && !location.isEmpty()) {
                requestBody.put("location", location);
            }
            if (employmentType != null && !employmentType.isEmpty()) {
                requestBody.put("employmentType", employmentType);
            }
            
            requestBody.put("page", page);
            requestBody.put("limit", limit);
            
            // 调用APIJobs
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            Map<String, String> headers = new HashMap<>();
            headers.put("apikey", apiJobsKey);
            
            String response = HttpApiUtil.postJson(
                    apiJobsBaseUrl + "/job/search",
                    jsonBody,
                    headers
            );
            
            // 解析响应
            JsonNode root = objectMapper.readTree(response);
            
            if (root.has("data")) {
                JsonNode data = root.get("data");
                List<Map<String, Object>> jobs = new ArrayList<>();
                
                for (JsonNode jobNode : data) {
                    Map<String, Object> job = new HashMap<>();
                    job.put("id", jobNode.path("id").asText(UUID.randomUUID().toString()));
                    job.put("title", jobNode.path("title").asText("未知职位"));
                    job.put("company", jobNode.path("company_name").asText("未知公司"));
                    job.put("location", jobNode.path("location").asText("未知地点"));
                    job.put("salary", formatSalary(jobNode.path("salary").asText("")));
                    job.put("description", truncateText(jobNode.path("description").asText(""), 200));
                    job.put("employmentType", jobNode.path("employment_type").asText("FULL_TIME"));
                    job.put("postedDate", jobNode.path("posted_date").asText(""));
                    job.put("url", jobNode.path("url").asText(""));
                    job.put("source", "APIJobs");
                    
                    jobs.add(job);
                }
                
                result.put("success", true);
                result.put("data", jobs);
                result.put("total", root.path("total").asInt(jobs.size()));
                result.put("page", page);
                result.put("limit", limit);
            } else {
                result.put("success", false);
                result.put("message", "API调用失败或配额用尽");
                result.put("data", getFallbackJobs(keyword));
            }
            
        } catch (Exception e) {
            log.error("搜索招聘数据失败: {}", e.getMessage());
            result.put("success", false);
            result.put("message", "服务暂时不可用，使用本地数据");
            result.put("data", getFallbackJobs(keyword));
        }
        
        return result;
    }

    /**
     * 获取推荐职位
     */
    @GetMapping("/jobs/recommended")
    public Map<String, Object> getRecommendedJobs(
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String experience,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> requestBody = new HashMap<>();
            if (skills != null && !skills.isEmpty()) {
                requestBody.put("q", skills);
            }
            requestBody.put("page", 1);
            requestBody.put("limit", limit);
            
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            Map<String, String> headers = new HashMap<>();
            headers.put("apikey", apiJobsKey);
            
            String response = HttpApiUtil.postJson(
                    apiJobsBaseUrl + "/job/search",
                    jsonBody,
                    headers
            );
            
            JsonNode root = objectMapper.readTree(response);
            
            if (root.has("data")) {
                List<Map<String, Object>> jobs = new ArrayList<>();
                JsonNode data = root.get("data");
                
                for (JsonNode jobNode : data) {
                    Map<String, Object> job = new HashMap<>();
                    job.put("id", jobNode.path("id").asText(UUID.randomUUID().toString()));
                    job.put("title", jobNode.path("title").asText("未知职位"));
                    job.put("company", jobNode.path("company_name").asText("未知公司"));
                    job.put("salary", formatSalary(jobNode.path("salary").asText("")));
                    job.put("location", jobNode.path("location").asText("未知地点"));
                    job.put("source", "APIJobs");
                    jobs.add(job);
                }
                
                result.put("success", true);
                result.put("data", jobs);
            } else {
                result.put("success", false);
                result.put("data", getRecommendedFallback());
            }
            
        } catch (Exception e) {
            log.error("获取推荐职位失败: {}", e.getMessage());
            result.put("success", false);
            result.put("data", getRecommendedFallback());
        }
        
        return result;
    }

    /**
     * 获取热门企业
     */
    @GetMapping("/companies/popular")
    public Map<String, Object> getPopularCompanies(
            @RequestParam(defaultValue = "10") Integer limit) {
        
        Map<String, Object> result = new HashMap<>();
        
        // 模拟热门企业数据（真实API需要企业信息API）
        List<Map<String, Object>> companies = new ArrayList<>();
        String[] popularCompanies = {
            "字节跳动", "阿里巴巴", "腾讯", "美团", "京东",
            "华为", "小米", "网易", "百度", "滴滴出行"
        };
        
        for (int i = 0; i < Math.min(limit, popularCompanies.length); i++) {
            Map<String, Object> company = new HashMap<>();
            company.put("name", popularCompanies[i]);
            company.put("industry", "互联网");
            company.put("logo", "");
            companies.add(company);
        }
        
        result.put("success", true);
        result.put("data", companies);
        return result;
    }

    /**
     * 格式化薪资
     */
    private String formatSalary(String salary) {
        if (salary == null || salary.isEmpty() || salary.equals("null")) {
            return "薪资面议";
        }
        return salary;
    }

    /**
     * 截断文本
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    /**
     * 兜底数据
     */
    private List<Map<String, Object>> getFallbackJobs(String keyword) {
        List<Map<String, Object>> jobs = new ArrayList<>();
        
        String[][] fallbackData = {
            {"Java开发工程师", "字节跳动", "北京", "25K-45K"},
            {"前端开发工程师", "阿里巴巴", "杭州", "22K-40K"},
            {"产品经理", "腾讯", "深圳", "20K-35K"},
            {"UI设计师", "美团", "北京", "15K-28K"},
            {"数据分析师", "京东", "北京", "18K-30K"}
        };
        
        for (String[] job : fallbackData) {
            Map<String, Object> jobMap = new HashMap<>();
            jobMap.put("id", UUID.randomUUID().toString());
            jobMap.put("title", job[0]);
            jobMap.put("company", job[1]);
            jobMap.put("location", job[2]);
            jobMap.put("salary", job[3]);
            jobMap.put("source", "本地数据");
            jobs.add(jobMap);
        }
        
        return jobs;
    }

    /**
     * 推荐职位兜底数据
     */
    private List<Map<String, Object>> getRecommendedFallback() {
        List<Map<String, Object>> jobs = new ArrayList<>();
        
        Map<String, Object> job1 = new HashMap<>();
        job1.put("id", "rec-101");
        job1.put("title", "Java后端开发工程师");
        job1.put("company", "字节跳动");
        job1.put("salary", "25K-45K");
        job1.put("location", "北京");
        job1.put("source", "推荐");
        jobs.add(job1);
        
        Map<String, Object> job2 = new HashMap<>();
        job2.put("id", "rec-102");
        job2.put("title", "前端开发工程师");
        job2.put("company", "阿里巴巴");
        job2.put("salary", "22K-40K");
        job2.put("location", "杭州");
        job2.put("source", "推荐");
        jobs.add(job2);
        
        Map<String, Object> job3 = new HashMap<>();
        job3.put("id", "rec-103");
        job3.put("title", "AI算法工程师");
        job3.put("company", "腾讯");
        job3.put("salary", "30K-55K");
        job3.put("location", "深圳");
        job3.put("source", "推荐");
        jobs.add(job3);
        
        Map<String, Object> job4 = new HashMap<>();
        job4.put("id", "rec-104");
        job4.put("title", "产品经理");
        job4.put("company", "美团");
        job4.put("salary", "20K-35K");
        job4.put("location", "北京");
        job4.put("source", "推荐");
        jobs.add(job4);
        
        return jobs;
    }
}
