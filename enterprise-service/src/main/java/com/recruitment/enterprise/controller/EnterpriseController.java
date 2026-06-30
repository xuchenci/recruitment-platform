package com.recruitment.enterprise.controller;

import com.recruitment.common.entity.Enterprise;
import com.recruitment.common.entity.Job;
import com.recruitment.common.entity.JobApplication;
import com.recruitment.common.entity.Student;
import com.recruitment.common.entity.User;
import com.recruitment.common.mapper.EnterpriseMapper;
import com.recruitment.common.mapper.JobMapper;
import com.recruitment.common.mapper.JobApplicationMapper;
import com.recruitment.common.mapper.StudentMapper;
import com.recruitment.common.mapper.UserMapper;
import com.recruitment.common.result.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer size) {
        Page<Enterprise> pageResult = enterpriseMapper.selectPage(
            new Page<>(page, size),
            new QueryWrapper<Enterprise>().orderByDesc("created_at")
        );
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        Enterprise enterprise = enterpriseMapper.selectById(id);
        return Result.success(enterprise);
    }

    @GetMapping("/by-user/{userId}")
    public Result<?> getByUserId(@PathVariable Long userId) {
        QueryWrapper<Enterprise> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        Enterprise enterprise = enterpriseMapper.selectOne(wrapper);
        if (enterprise == null) {
            enterprise = new Enterprise();
            enterprise.setUserId(userId);
            enterprise.setVerifyStatus(0);
            enterprise.setVerified(0);
        }
        return Result.success(enterprise);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Enterprise enterprise) {
        enterpriseMapper.insert(enterprise);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Enterprise enterprise) {
        enterpriseMapper.updateById(enterprise);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        enterpriseMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @PostMapping("/verify")
    public Result<?> submitVerify(@RequestBody Map<String, Object> verifyData) {
        Long userId = verifyData.get("userId") != null ? Long.valueOf(verifyData.get("userId").toString()) : null;
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        QueryWrapper<Enterprise> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        Enterprise enterprise = enterpriseMapper.selectOne(wrapper);
        
        if (enterprise == null) {
            enterprise = new Enterprise();
            enterprise.setUserId(userId);
            enterprise.setVerifyStatus(0);
            enterprise.setVerified(0);
        }

        if (verifyData.containsKey("enterpriseName")) {
            enterprise.setCompanyName((String) verifyData.get("enterpriseName"));
        }
        if (verifyData.containsKey("creditCode")) {
            enterprise.setCreditCode((String) verifyData.get("creditCode"));
        }
        if (verifyData.containsKey("industry")) {
            enterprise.setIndustry((String) verifyData.get("industry"));
        }
        if (verifyData.containsKey("scale")) {
            enterprise.setCompanySize((String) verifyData.get("scale"));
        }
        if (verifyData.containsKey("financingStage")) {
            enterprise.setFinancingStage((String) verifyData.get("financingStage"));
        }
        if (verifyData.containsKey("city")) {
            enterprise.setCity((String) verifyData.get("city"));
        }
        if (verifyData.containsKey("address")) {
            enterprise.setAddress((String) verifyData.get("address"));
        }
        if (verifyData.containsKey("description")) {
            enterprise.setDescription((String) verifyData.get("description"));
        }
        if (verifyData.containsKey("contactPerson")) {
            enterprise.setContactName((String) verifyData.get("contactPerson"));
        }
        if (verifyData.containsKey("contactPosition")) {
            enterprise.setContactPosition((String) verifyData.get("contactPosition"));
        }
        if (verifyData.containsKey("contactPhone")) {
            enterprise.setContactPhone((String) verifyData.get("contactPhone"));
        }
        if (verifyData.containsKey("contactEmail")) {
            enterprise.setContactEmail((String) verifyData.get("contactEmail"));
        }
        if (verifyData.containsKey("businessLicenseUrl")) {
            enterprise.setBusinessLicenseUrl((String) verifyData.get("businessLicenseUrl"));
        }
        if (verifyData.containsKey("otherCertUrl")) {
            enterprise.setOtherCertUrl((String) verifyData.get("otherCertUrl"));
        }
        
        enterprise.setVerifyStatus(1);
        enterprise.setVerifyTime(LocalDateTime.now());
        
        if (enterprise.getId() == null) {
            enterpriseMapper.insert(enterprise);
        } else {
            enterpriseMapper.updateById(enterprise);
        }
        
        return Result.success("认证申请已提交，请等待审核");
    }

    @GetMapping("/verify-status")
    public Result<?> getVerifyStatus(@RequestParam(required = false) Long userId,
                                     @RequestParam(required = false) Long enterpriseId) {
        Enterprise enterprise = null;
        
        if (userId != null) {
            QueryWrapper<Enterprise> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            enterprise = enterpriseMapper.selectOne(wrapper);
        } else if (enterpriseId != null) {
            enterprise = enterpriseMapper.selectById(enterpriseId);
        }
        
        Map<String, Object> result = new HashMap<>();
        
        if (enterprise == null) {
            result.put("verifyStatus", 0);
            result.put("status", "未提交");
            result.put("rejectReason", "");
            result.put("data", new HashMap<>());
            return Result.success(result);
        }
        
        result.put("enterpriseId", enterprise.getId());
        result.put("companyName", enterprise.getCompanyName());
        result.put("verifyStatus", enterprise.getVerifyStatus());
        result.put("rejectReason", enterprise.getRejectReason());
        
        String statusText = "";
        Integer status = enterprise.getVerifyStatus();
        if (status == null || status == 0) {
            statusText = "未提交";
        } else if (status == 1) {
            statusText = "审核中";
        } else if (status == 2) {
            statusText = "已通过";
        } else if (status == 3) {
            statusText = "已拒绝";
        }
        result.put("status", statusText);
        
        Map<String, Object> data = new HashMap<>();
        data.put("enterpriseName", enterprise.getCompanyName());
        data.put("creditCode", enterprise.getCreditCode());
        data.put("industry", enterprise.getIndustry());
        data.put("scale", enterprise.getCompanySize());
        data.put("financingStage", enterprise.getFinancingStage());
        data.put("city", enterprise.getCity());
        data.put("address", enterprise.getAddress());
        data.put("description", enterprise.getDescription());
        data.put("contactPerson", enterprise.getContactName());
        data.put("contactPosition", enterprise.getContactPosition());
        data.put("contactPhone", enterprise.getContactPhone());
        data.put("contactEmail", enterprise.getContactEmail());
        data.put("businessLicenseUrl", enterprise.getBusinessLicenseUrl());
        data.put("otherCertUrl", enterprise.getOtherCertUrl());
        result.put("data", data);
        
        return Result.success(result);
    }

    @PostMapping("/{enterpriseId}/pass-verify")
    public Result<?> passVerify(@PathVariable Long enterpriseId) {
        Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
        if (enterprise == null) {
            return Result.error("企业不存在");
        }
        
        enterprise.setVerifyStatus(2);
        enterprise.setVerified(1);
        enterprise.setVerifyTime(LocalDateTime.now());
        enterprise.setRejectReason(null);
        
        enterpriseMapper.updateById(enterprise);
        return Result.success("认证审核通过");
    }

    @PostMapping("/{enterpriseId}/reject-verify")
    public Result<?> rejectVerify(@PathVariable Long enterpriseId, @RequestBody Map<String, String> body) {
        Enterprise enterprise = enterpriseMapper.selectById(enterpriseId);
        if (enterprise == null) {
            return Result.error("企业不存在");
        }
        
        enterprise.setVerifyStatus(3);
        enterprise.setVerified(0);
        enterprise.setVerifyTime(LocalDateTime.now());
        enterprise.setRejectReason(body.get("rejectReason"));
        
        enterpriseMapper.updateById(enterprise);
        return Result.success("认证审核拒绝");
    }

    @GetMapping("/verify/{id}")
    public Result<?> verify(@PathVariable Long id) {
        Enterprise enterprise = enterpriseMapper.selectById(id);
        if (enterprise != null) {
            enterprise.setVerified(1);
            enterprise.setVerifyStatus(2);
            enterprise.setVerifyTime(LocalDateTime.now());
            enterpriseMapper.updateById(enterprise);
        }
        return Result.success("认证通过");
    }

    @GetMapping("/search")
    public Result<?> search(@RequestParam String keyword) {
        List<Enterprise> list = enterpriseMapper.selectList(
            new QueryWrapper<Enterprise>().like("company_name", keyword)
        );
        return Result.success(list);
    }

    @GetMapping("/dashboard/{enterpriseId}")
    public Result<?> dashboard(@PathVariable Long enterpriseId) {
        Map<String, Object> data = new HashMap<>();
        
        QueryWrapper<Job> jobWrapper = new QueryWrapper<>();
        jobWrapper.eq("enterprise_id", enterpriseId);
        Long totalJobs = jobMapper.selectCount(jobWrapper);
        data.put("totalJobs", totalJobs);
        
        QueryWrapper<Job> activeJobWrapper = new QueryWrapper<>();
        activeJobWrapper.eq("enterprise_id", enterpriseId).eq("status", 1);
        Long activeJobCount = jobMapper.selectCount(activeJobWrapper);
        data.put("activeJobCount", activeJobCount);
        
        QueryWrapper<Job> closedJobWrapper = new QueryWrapper<>();
        closedJobWrapper.eq("enterprise_id", enterpriseId).eq("status", 0);
        Long closedJobCount = jobMapper.selectCount(closedJobWrapper);
        data.put("closedJobCount", closedJobCount);
        
        QueryWrapper<JobApplication> appWrapper = new QueryWrapper<>();
        appWrapper.eq("enterprise_id", enterpriseId);
        Long totalApplications = jobApplicationMapper.selectCount(appWrapper);
        data.put("totalApplications", totalApplications);
        
        QueryWrapper<JobApplication> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("enterprise_id", enterpriseId).eq("status", 1);
        Long pendingCount = jobApplicationMapper.selectCount(pendingWrapper);
        data.put("pendingCount", pendingCount);
        
        QueryWrapper<JobApplication> interviewWrapper = new QueryWrapper<>();
        interviewWrapper.eq("enterprise_id", enterpriseId).eq("status", 3);
        Long interviewCount = jobApplicationMapper.selectCount(interviewWrapper);
        data.put("interviewCount", interviewCount);
        
        QueryWrapper<JobApplication> hireWrapper = new QueryWrapper<>();
        hireWrapper.eq("enterprise_id", enterpriseId).eq("status", 4);
        Long hireCount = jobApplicationMapper.selectCount(hireWrapper);
        data.put("hireCount", hireCount);
        
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();
        
        QueryWrapper<JobApplication> todayInterviewWrapper = new QueryWrapper<>();
        todayInterviewWrapper.eq("enterprise_id", enterpriseId).eq("status", 3)
            .ge("interview_time", todayStart).lt("interview_time", todayEnd);
        Long todayInterviewCount = jobApplicationMapper.selectCount(todayInterviewWrapper);
        data.put("todayInterviewCount", todayInterviewCount);
        
        LocalDateTime weekStart = today.minusDays(7).atStartOfDay();
        QueryWrapper<JobApplication> weekPassWrapper = new QueryWrapper<>();
        weekPassWrapper.eq("enterprise_id", enterpriseId).eq("status", 4)
            .ge("updated_at", weekStart);
        Long weekPassCount = jobApplicationMapper.selectCount(weekPassWrapper);
        data.put("weekPassCount", weekPassCount);
        
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();
        QueryWrapper<JobApplication> monthHireWrapper = new QueryWrapper<>();
        monthHireWrapper.eq("enterprise_id", enterpriseId).eq("status", 4)
            .ge("updated_at", monthStart);
        Long monthHireCount = jobApplicationMapper.selectCount(monthHireWrapper);
        data.put("monthHireCount", monthHireCount);
        
        int totalViewCount = 0;
        List<Job> jobs = jobMapper.selectList(activeJobWrapper);
        for (Job job : jobs) {
            totalViewCount += job.getViewCount() != null ? job.getViewCount() : 0;
        }
        data.put("totalViewCount", totalViewCount);
        
        List<Map<String, Object>> activeJobs = new ArrayList<>();
        for (Job job : jobs) {
            Map<String, Object> jobMap = new HashMap<>();
            jobMap.put("id", job.getId());
            jobMap.put("title", job.getJobTitle());
            jobMap.put("location", job.getCity());
            jobMap.put("experience", job.getExperienceRequirement());
            jobMap.put("education", job.getEducationRequirement());
            jobMap.put("applicationCount", job.getApplyCount() != null ? job.getApplyCount() : 0);
            jobMap.put("salaryMin", job.getSalaryMin());
            jobMap.put("salaryMax", job.getSalaryMax());
            activeJobs.add(jobMap);
        }
        data.put("activeJobs", activeJobs);
        
        QueryWrapper<JobApplication> recentAppWrapper = new QueryWrapper<>();
        recentAppWrapper.eq("enterprise_id", enterpriseId)
            .orderByDesc("created_at")
            .last("LIMIT 5");
        List<JobApplication> recentApps = jobApplicationMapper.selectList(recentAppWrapper);
        
        List<Map<String, Object>> recentResumes = new ArrayList<>();
        for (JobApplication app : recentApps) {
            Map<String, Object> resumeMap = new HashMap<>();
            resumeMap.put("id", app.getId());
            resumeMap.put("applicationId", app.getId());
            resumeMap.put("resumeId", app.getResumeId());
            resumeMap.put("jobId", app.getJobId());
            
            Student student = studentMapper.selectById(app.getStudentId());
            User user = student != null ? userMapper.selectById(student.getUserId()) : null;
            Job job = jobMapper.selectById(app.getJobId());
            
            resumeMap.put("studentName", user != null && user.getRealName() != null ? user.getRealName() : "未知");
            resumeMap.put("school", student != null ? student.getCollege() : "");
            resumeMap.put("major", student != null ? student.getMajor() : "");
            resumeMap.put("degree", student != null ? student.getDegree() : "");
            resumeMap.put("jobTitle", job != null ? job.getJobTitle() : "未知职位");
            
            int statusCode = app.getStatus() != null ? app.getStatus() : 1;
            String statusStr = "待处理";
            if (statusCode == 1) statusStr = "待处理";
            else if (statusCode == 2) statusStr = "已查看";
            else if (statusCode == 3) statusStr = "面试中";
            else if (statusCode == 4) statusStr = "已录用";
            else if (statusCode == 5) statusStr = "已拒绝";
            resumeMap.put("status", statusStr);
            resumeMap.put("statusCode", statusCode);
            
            resumeMap.put("createTime", app.getApplyTime() != null ? 
                app.getApplyTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            
            recentResumes.add(resumeMap);
        }
        data.put("recentResumes", recentResumes);
        
        return Result.success(data);
    }

    @GetMapping("/pending-list")
    public Result<?> pendingList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size) {
        Page<Enterprise> pageResult = enterpriseMapper.selectPage(
            new Page<>(page, size),
            new QueryWrapper<Enterprise>().eq("verify_status", 1).orderByDesc("verify_time")
        );
        return Result.success(pageResult);
    }
}