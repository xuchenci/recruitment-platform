package com.recruitment.application.controller;

import com.recruitment.common.entity.JobApplication;
import com.recruitment.common.entity.Student;
import com.recruitment.common.entity.Job;
import com.recruitment.common.entity.User;
import com.recruitment.common.entity.Resume;
import com.recruitment.common.entity.Enterprise;
import com.recruitment.common.mapper.JobApplicationMapper;
import com.recruitment.common.mapper.StudentMapper;
import com.recruitment.common.mapper.JobMapper;
import com.recruitment.common.mapper.UserMapper;
import com.recruitment.common.mapper.ResumeMapper;
import com.recruitment.common.mapper.EnterpriseMapper;
import com.recruitment.common.result.Result;
import com.recruitment.common.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private JobApplicationMapper jobApplicationMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ResumeMapper resumeMapper;
    
    @Autowired
    private EnterpriseMapper enterpriseMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取我的投递记录（学生端）
     */
    @GetMapping("/my")
    public Result<?> getMyApplications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            String t = token.substring(7);
            try {
                userId = jwtUtil.getUserIdFromToken(t);
            } catch (Exception e) {
                return Result.error(401, "登录已过期");
            }
        }
        if (userId == null) {
            userId = 11L;
        }

            QueryWrapper<JobApplication> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);

            // 状态映射：前端字符串 -> 数据库数字
            if (status != null && !status.isEmpty()) {
                Integer statusCode = mapStatusToCode(status);
                if (statusCode != null) {
                    wrapper.eq("status", statusCode);
                }
            }

            if (keyword != null && !keyword.isEmpty()) {
                // 先查找匹配的jobId
                QueryWrapper<Job> jobWrapper = new QueryWrapper<>();
                jobWrapper.like("job_title", keyword);
                List<Job> matchedJobs = jobMapper.selectList(jobWrapper);
                if (!matchedJobs.isEmpty()) {
                    List<Long> jobIds = matchedJobs.stream().map(Job::getId).toList();
                    wrapper.in("job_id", jobIds);
                } else {
                    // 没有匹配的职位，返回空
                    Map<String, Object> emptyResult = new HashMap<>();
                    emptyResult.put("records", new ArrayList<>());
                    emptyResult.put("total", 0);
                    emptyResult.put("current", page);
                    emptyResult.put("size", size);
                    return Result.success(emptyResult);
                }
            }

            wrapper.orderByDesc("created_at");
            Page<JobApplication> pageResult = jobApplicationMapper.selectPage(new Page<>(page, size), wrapper);

            // 组装返回数据
            List<Map<String, Object>> records = new ArrayList<>();
            for (JobApplication app : pageResult.getRecords()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", app.getId());
                item.put("jobId", app.getJobId());
                item.put("userId", app.getUserId());
                item.put("resumeId", app.getResumeId());
                item.put("status", mapCodeToStatus(app.getStatus()));
                item.put("createTime", app.getCreatedAt() != null ? app.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
                item.put("interviewTime", app.getInterviewTime() != null ? app.getInterviewTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
                item.put("interviewLocation", app.getInterviewLocation());
                item.put("remark", app.getFeedback());
                item.put("coverLetter", app.getCoverLetter());

                // 查询职位信息
                if (app.getJobId() != null) {
                    Job job = jobMapper.selectById(app.getJobId());
                    if (job != null) {
                        item.put("jobTitle", job.getJobTitle());
                        item.put("location", job.getCity());
                        item.put("salaryRange", (job.getSalaryMin() != null && job.getSalaryMax() != null) 
                            ? job.getSalaryMin() + "K-" + job.getSalaryMax() + "K" : "面议");
                        // 查询企业名称
                        String enterpriseName = "";
                        if (job.getEnterpriseId() != null) {
                            Enterprise enterprise = enterpriseMapper.selectById(job.getEnterpriseId());
                            if (enterprise != null) {
                                enterpriseName = enterprise.getCompanyName();
                            }
                        }
                        item.put("enterpriseName", enterpriseName);
                    } else {
                        item.put("jobTitle", "未知职位");
                        item.put("enterpriseName", "");
                        item.put("location", "");
                        item.put("salaryRange", "");
                    }
                }

                records.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", pageResult.getTotal());
            result.put("current", pageResult.getCurrent());
            result.put("size", pageResult.getSize());

            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取投递记录失败: " + e.getMessage());
        }
    }

    /**
     * 状态码映射：字符串 -> 数字
     * 数据库定义：1-已投递，2-已查看，3-面试中，4-已录用，5-已拒绝，6-已取消
     */
    private Integer mapStatusToCode(String status) {
        return switch (status) {
            case "pending" -> 1;
            case "reviewed" -> 2;
            case "interviewing" -> 3;
            case "rejected" -> 5;
            case "offered" -> 4;
            case "withdrawn" -> 6;
            default -> null;
        };
    }

    /**
     * 状态码映射：数字 -> 字符串
     * 数据库定义：1-已投递，2-已查看，3-面试中，4-已录用，5-已拒绝，6-已取消
     */
    private String mapCodeToStatus(Integer code) {
        if (code == null) return "pending";
        return switch (code) {
            case 1 -> "pending";
            case 2 -> "reviewed";
            case 3 -> "interviewing";
            case 4 -> "offered";
            case 5 -> "rejected";
            case 6 -> "withdrawn";
            default -> "pending";
        };
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Long userId,
                          @RequestParam(required = false) Long jobId) {
        QueryWrapper<JobApplication> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (jobId != null) {
            wrapper.eq("job_id", jobId);
        }
        wrapper.orderByDesc("created_at");
        Page<JobApplication> pageResult = jobApplicationMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        JobApplication app = jobApplicationMapper.selectById(id);
        return Result.success(app);
    }

    @PostMapping("/apply")
    public Result<?> apply(@RequestBody Map<String, Object> params,
                           @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long jobId = ((Number) params.get("jobId")).longValue();
            Long resumeId = params.get("resumeId") != null ? ((Number) params.get("resumeId")).longValue() : null;
            String coverLetter = params.get("coverLetter") != null ? params.get("coverLetter").toString() : "";

            Job job = jobMapper.selectById(jobId);
            if (job == null) {
                return Result.error(404, "职位不存在");
            }

            Long userId = null;
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    userId = jwtUtil.getUserIdFromToken(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (userId == null) {
                userId = (params.get("userId") != null) ? ((Number) params.get("userId")).longValue() : null;
            }

            if (userId == null) {
                return Result.error(401, "用户未登录");
            }

            Student student = studentMapper.selectOne(new QueryWrapper<Student>().eq("user_id", userId));
            if (student == null) {
                return Result.error(404, "请先完善学生信息后再投递");
            }

            // 验证简历是否存在且属于该学生
            if (resumeId != null) {
                Resume resume = resumeMapper.selectById(resumeId);
                if (resume == null) {
                    // 简历不存在，不设置resumeId，避免FK约束失败
                    resumeId = null;
                } else if (!resume.getStudentId().equals(student.getId())) {
                    return Result.error(403, "简历不属于当前用户");
                }
            }

            JobApplication application = new JobApplication();
            application.setUserId(userId);
            application.setStudentId(student.getId());
            application.setJobId(jobId);
            application.setEnterpriseId(job.getEnterpriseId());
            application.setResumeId(resumeId);
            application.setCoverLetter(coverLetter);
            application.setStatus(1);
            application.setApplyTime(LocalDateTime.now());
            application.setCreatedAt(LocalDateTime.now());
            application.setUpdatedAt(LocalDateTime.now());

            jobApplicationMapper.insert(application);

            job.setApplyCount(job.getApplyCount() != null ? job.getApplyCount() + 1 : 1);
            jobMapper.updateById(job);

            return Result.success("投递成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "投递失败: " + e.getMessage());
        }
    }

    @PutMapping("/status")
    public Result<?> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = ((Number) params.get("id")).longValue();
        Integer status = ((Number) params.get("status")).intValue();
        JobApplication app = jobApplicationMapper.selectById(id);
        if (app != null) {
            app.setStatus(status);
            jobApplicationMapper.updateById(app);
        }
        return Result.success("状态更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        jobApplicationMapper.deleteById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/inbox")
    public Result<?> inbox(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer size,
                           @RequestParam Long enterpriseId) {
        QueryWrapper<JobApplication> wrapper = new QueryWrapper<>();
        wrapper.eq("enterprise_id", enterpriseId);
        wrapper.orderByDesc("created_at");
        Page<JobApplication> pageResult = jobApplicationMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @GetMapping("/interview")
    public Result<?> interviews(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer size,
                               @RequestParam Long enterpriseId,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate) {
        QueryWrapper<JobApplication> wrapper = new QueryWrapper<>();
        wrapper.eq("enterprise_id", enterpriseId);
        wrapper.in("status", 2, 3, 4);
        
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge("interview_time", startDate + " 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le("interview_time", endDate + " 23:59:59");
        }
        
        wrapper.orderByDesc("interview_time");
        Page<JobApplication> pageResult = jobApplicationMapper.selectPage(new Page<>(page, size), wrapper);
        
        List<Map<String, Object>> resultList = pageResult.getRecords().stream().map(app -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            
            Student student = studentMapper.selectById(app.getStudentId());
            User user = student != null ? userMapper.selectById(student.getUserId()) : null;
            Job job = jobMapper.selectById(app.getJobId());
            
            if (student != null) {
                map.put("studentName", user != null ? user.getRealName() : "未知");
                map.put("school", student.getCollege());
                map.put("major", student.getMajor());
            } else {
                map.put("studentName", "未知");
                map.put("school", "");
                map.put("major", "");
            }
            
            if (job != null) {
                map.put("jobTitle", job.getJobTitle());
            } else {
                map.put("jobTitle", "未知职位");
            }
            
            map.put("interviewTime", formatDateTime(app.getInterviewTime()));
            map.put("type", app.getInterviewLocation() != null && app.getInterviewLocation().contains("会议") ? "online" : "onsite");
            map.put("location", app.getInterviewLocation() != null ? app.getInterviewLocation() : "-");
            
            if (app.getStatus() == 3) {
                map.put("result", "pass");
            } else if (app.getStatus() == 4) {
                map.put("result", "fail");
            } else {
                map.put("result", null);
            }
            
            return map;
        }).filter(map -> {
            if (keyword == null || keyword.isEmpty()) {
                return true;
            }
            String kw = keyword.toLowerCase();
            String studentName = map.get("studentName") != null ? map.get("studentName").toString().toLowerCase() : "";
            String jobTitle = map.get("jobTitle") != null ? map.get("jobTitle").toString().toLowerCase() : "";
            String school = map.get("school") != null ? map.get("school").toString().toLowerCase() : "";
            return studentName.contains(kw) || jobTitle.contains(kw) || school.contains(kw);
        }).toList();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultList);
        result.put("total", resultList.size());
        result.put("current", pageResult.getCurrent());
        result.put("size", pageResult.getSize());
        
        return Result.success(result);
    }

    @GetMapping("/dashboard")
    public Result<?> dashboard(@RequestParam Long enterpriseId) {
        Map<String, Object> data = new HashMap<>();

        // 该企业的总投递数
        QueryWrapper<JobApplication> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("enterprise_id", enterpriseId);
        Long totalApplications = jobApplicationMapper.selectCount(totalWrapper);
        data.put("totalApplications", totalApplications.intValue());

        // 待处理数（status=1）
        QueryWrapper<JobApplication> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("enterprise_id", enterpriseId).eq("status", 1);
        Long pendingCount = jobApplicationMapper.selectCount(pendingWrapper);
        data.put("pendingCount", pendingCount.intValue());

        // 面试中数（status=3）
        QueryWrapper<JobApplication> interviewWrapper = new QueryWrapper<>();
        interviewWrapper.eq("enterprise_id", enterpriseId).eq("status", 3);
        Long interviewCount = jobApplicationMapper.selectCount(interviewWrapper);
        data.put("interviewCount", interviewCount.intValue());

        // 已录用数（status=4）
        QueryWrapper<JobApplication> hiredWrapper = new QueryWrapper<>();
        hiredWrapper.eq("enterprise_id", enterpriseId).eq("status", 4);
        Long hiredCount = jobApplicationMapper.selectCount(hiredWrapper);
        data.put("hiredCount", hiredCount.intValue());

        // 该企业发布的职位数
        QueryWrapper<Job> jobWrapper = new QueryWrapper<>();
        jobWrapper.eq("enterprise_id", enterpriseId);
        Long totalJobs = jobMapper.selectCount(jobWrapper);
        data.put("totalJobs", totalJobs.intValue());

        return Result.success(data);
    }

    /**
     * 企业端简历收件箱查询接口
     * 支持按姓名/学校/专业搜索，按职位、状态筛选
     */
    @GetMapping("/inbox/search")
    public Result<?> searchInbox(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Long enterpriseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String status) {
        
        QueryWrapper<JobApplication> wrapper = new QueryWrapper<>();
        wrapper.eq("enterprise_id", enterpriseId);
        
        // 状态转换：前端使用字符串状态，后端使用数字状态
        if (status != null && !status.isEmpty()) {
            Map<String, Integer> statusMap = new HashMap<>();
            statusMap.put("pending", 1);
            statusMap.put("shortlisted", 2);
            statusMap.put("rejected", 5);
            statusMap.put("interviewed", 3);
            Integer statusCode = statusMap.get(status);
            if (statusCode != null) {
                wrapper.eq("status", statusCode);
            }
        }
        
        if (jobId != null) {
            wrapper.eq("job_id", jobId);
        }
        
        wrapper.orderByDesc("created_at");
        
        Page<JobApplication> pageResult = jobApplicationMapper.selectPage(new Page<>(page, size), wrapper);
        
        // 组装完整数据
        List<Map<String, Object>> resultList = pageResult.getRecords().stream().map(app -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            map.put("status", convertStatus(app.getStatus()));
            
            // 获取学生信息
            Student student = studentMapper.selectById(app.getStudentId());
            // 获取用户信息（姓名、电话、邮箱等存储在User表中）
            User user = student != null ? userMapper.selectById(student.getUserId()) : null;
            
            if (student != null) {
                map.put("studentName", user != null ? user.getRealName() : "未知");
                map.put("school", student.getCollege());
                map.put("major", student.getMajor());
                map.put("degree", student.getDegree());
                map.put("phone", user != null ? user.getPhone() : "");
                map.put("email", user != null ? user.getEmail() : "");
                map.put("gender", student.getGender() != null && student.getGender() == 2 ? "女" : "男");
                map.put("expectedSalary", student.getExpectationSalary() != null ? student.getExpectationSalary() + "K" : "面议");
                map.put("summary", student.getBio());
                map.put("skills", student.getSkills());
            } else {
                map.put("studentName", "未知");
                map.put("school", "");
                map.put("major", "");
                map.put("degree", "");
            }
            
            // 获取职位信息
            Job job = jobMapper.selectById(app.getJobId());
            if (job != null) {
                map.put("jobTitle", job.getJobTitle());
                map.put("jobId", job.getId());
            } else {
                map.put("jobTitle", "未知职位");
                map.put("jobId", app.getJobId());
            }
            
            // 计算匹配度（模拟）
            map.put("matchScore", Math.min(100, 60 + (int)(Math.random() * 40)));
            map.put("isRecommended", Math.random() > 0.7);
            map.put("createTime", formatDateTime(app.getCreatedAt()));
            
            return map;
        }).filter(map -> {
            if (keyword == null || keyword.isEmpty()) {
                return true;
            }
            String kw = keyword.toLowerCase();
            String studentName = map.get("studentName") != null ? map.get("studentName").toString().toLowerCase() : "";
            String school = map.get("school") != null ? map.get("school").toString().toLowerCase() : "";
            String major = map.get("major") != null ? map.get("major").toString().toLowerCase() : "";
            String jobTitle = map.get("jobTitle") != null ? map.get("jobTitle").toString().toLowerCase() : "";
            return studentName.contains(kw) || school.contains(kw) || major.contains(kw) || jobTitle.contains(kw);
        }).toList();
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", resultList);
        result.put("total", resultList.size());
        result.put("current", pageResult.getCurrent());
        result.put("size", pageResult.getSize());
        
        return Result.success(result);
    }

    /**
     * 导出Excel接口
     */
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam Long enterpriseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String status,
            HttpServletResponse response) {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("简历列表");
            
            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            // 创建数据行样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"序号", "求职者姓名", "性别", "学校", "专业", "学历", "应聘岗位", "匹配度", "状态", "投递时间", "手机号", "邮箱"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 查询数据
            QueryWrapper<JobApplication> wrapper = new QueryWrapper<>();
            wrapper.eq("enterprise_id", enterpriseId);
            
            if (status != null && !status.isEmpty()) {
                Map<String, Integer> statusMap = new HashMap<>();
                statusMap.put("pending", 1);
                statusMap.put("shortlisted", 2);
                statusMap.put("rejected", 5);
                statusMap.put("interviewed", 3);
                Integer statusCode = statusMap.get(status);
                if (statusCode != null) {
                    wrapper.eq("status", statusCode);
                }
            }
            
            if (jobId != null) {
                wrapper.eq("job_id", jobId);
            }
            
            wrapper.orderByDesc("created_at");
            
            List<JobApplication> applications = jobApplicationMapper.selectList(wrapper);
            
            // 填充数据
            int rowNum = 1;
            for (JobApplication app : applications) {
                Row row = sheet.createRow(rowNum);
                
                Student student = studentMapper.selectById(app.getStudentId());
                User user = student != null ? userMapper.selectById(student.getUserId()) : null;
                Job job = jobMapper.selectById(app.getJobId());
                
                createCell(row, 0, rowNum, dataStyle);
                createCell(row, 1, user != null && user.getRealName() != null ? user.getRealName() : "未知", dataStyle);
                createCell(row, 2, student != null && student.getGender() != null && student.getGender() == 2 ? "女" : "男", dataStyle);
                createCell(row, 3, student != null ? student.getCollege() : "", dataStyle);
                createCell(row, 4, student != null ? student.getMajor() : "", dataStyle);
                createCell(row, 5, student != null ? student.getDegree() : "", dataStyle);
                createCell(row, 6, job != null ? job.getJobTitle() : "未知职位", dataStyle);
                createCell(row, 7, (60 + (int)(Math.random() * 40)) + "%", dataStyle);
                createCell(row, 8, convertStatus(app.getStatus()), dataStyle);
                createCell(row, 9, formatDateTime(app.getCreatedAt()), dataStyle);
                createCell(row, 10, user != null ? user.getPhone() : "", dataStyle);
                createCell(row, 11, user != null ? user.getEmail() : "", dataStyle);
                
                rowNum++;
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // 设置最小宽度
                if (sheet.getColumnWidth(i) < 3000) {
                    sheet.setColumnWidth(i, 3000);
                }
            }
            
            // 设置响应头
            String fileName = "简历列表_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setCharacterEncoding("UTF-8");
            
            // 写入输出流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private String convertStatus(Integer status) {
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(1, "待处理");
        statusMap.put(2, "已通过初筛");
        statusMap.put(3, "已安排面试");
        statusMap.put(4, "已录用");
        statusMap.put(5, "已拒绝");
        statusMap.put(6, "已取消");
        return statusMap.getOrDefault(status, "未知");
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * 企业端查看投递者简历详情
     */
    @GetMapping("/{applicationId}/resume")
    public Result<?> viewResume(@PathVariable Long applicationId) {
        try {
            JobApplication application = jobApplicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error(404, "投递记录不存在");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("applicationId", application.getId());
            result.put("jobId", application.getJobId());
            result.put("status", convertStatus(application.getStatus()));
            result.put("applyTime", formatDateTime(application.getApplyTime()));
            result.put("coverLetter", application.getCoverLetter());
            
            Job job = jobMapper.selectById(application.getJobId());
            if (job != null) {
                result.put("jobTitle", job.getJobTitle());
            }
            
            Student student = studentMapper.selectById(application.getStudentId());
            User user = student != null ? userMapper.selectById(student.getUserId()) : null;
            
            if (student != null) {
                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("name", user != null ? user.getRealName() : "");
                studentInfo.put("phone", user != null ? user.getPhone() : "");
                studentInfo.put("email", user != null ? user.getEmail() : "");
                studentInfo.put("gender", student.getGender() != null && student.getGender() == 2 ? "女" : "男");
                studentInfo.put("college", student.getCollege());
                studentInfo.put("major", student.getMajor());
                studentInfo.put("degree", student.getDegree());
                studentInfo.put("graduationYear", student.getGraduationYear());
                studentInfo.put("bio", student.getBio());
                studentInfo.put("skills", student.getSkills());
                studentInfo.put("expectationSalary", student.getExpectationSalary());
                studentInfo.put("expectationPosition", student.getExpectationPosition());
                result.put("student", studentInfo);
            }
            
            Resume resume = null;
            if (application.getResumeId() != null) {
                resume = resumeMapper.selectById(application.getResumeId());
            }
            
            if (resume == null && student != null) {
                resume = resumeMapper.selectOne(new QueryWrapper<Resume>().eq("student_id", student.getId()));
            }
            
            if (resume != null) {
                Map<String, Object> resumeInfo = new HashMap<>();
                resumeInfo.put("id", resume.getId());
                resumeInfo.put("realName", resume.getRealName());
                resumeInfo.put("gender", resume.getGender());
                resumeInfo.put("birthDate", resume.getBirthDate());
                resumeInfo.put("phone", resume.getPhone());
                resumeInfo.put("email", resume.getEmail());
                resumeInfo.put("city", resume.getCity());
                resumeInfo.put("summary", resume.getSummary());
                resumeInfo.put("expectedPosition", resume.getExpectedPosition());
                resumeInfo.put("expectedCity", resume.getExpectedCity());
                resumeInfo.put("expectedSalary", resume.getExpectedSalary());
                resumeInfo.put("completeness", resume.getCompleteness());
                
                try {
                    resumeInfo.put("educations", objectMapper.readValue(resume.getEducations() != null ? resume.getEducations() : "[]", List.class));
                    resumeInfo.put("experiences", objectMapper.readValue(resume.getExperiences() != null ? resume.getExperiences() : "[]", List.class));
                    resumeInfo.put("projects", objectMapper.readValue(resume.getProjects() != null ? resume.getProjects() : "[]", List.class));
                    resumeInfo.put("skills", objectMapper.readValue(resume.getSkills() != null ? resume.getSkills() : "[]", List.class));
                    resumeInfo.put("languages", objectMapper.readValue(resume.getLanguages() != null ? resume.getLanguages() : "[]", List.class));
                    resumeInfo.put("certificates", objectMapper.readValue(resume.getCertificates() != null ? resume.getCertificates() : "[]", List.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                result.put("resume", resumeInfo);
            }
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取简历失败: " + e.getMessage());
        }
    }

    /**
     * 安排面试
     */
    @PostMapping("/{applicationId}/interview")
    public Result<?> scheduleInterview(@PathVariable Long applicationId, @RequestBody Map<String, Object> params) {
        try {
            JobApplication application = jobApplicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error(404, "投递记录不存在");
            }
            
            String interviewTime = params.get("interviewTime") != null ? params.get("interviewTime").toString() : null;
            String type = params.get("type") != null ? params.get("type").toString() : "onsite";
            String location = params.get("location") != null ? params.get("location").toString() : "";
            String remark = params.get("remark") != null ? params.get("remark").toString() : "";
            
            if (interviewTime == null || interviewTime.isEmpty()) {
                return Result.error("面试时间不能为空");
            }
            
            application.setStatus(3);
            application.setInterviewTime(LocalDateTime.parse(interviewTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            application.setInterviewLocation(location);
            application.setFeedback(remark);
            application.setUpdateTime(LocalDateTime.now());
            
            jobApplicationMapper.updateById(application);
            
            return Result.success("面试安排成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "安排面试失败: " + e.getMessage());
        }
    }

    /**
     * 更新面试结果
     */
    @PutMapping("/{applicationId}/interview-result")
    public Result<?> updateInterviewResult(@PathVariable Long applicationId, @RequestBody Map<String, Object> params) {
        try {
            JobApplication application = jobApplicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error(404, "投递记录不存在");
            }
            
            String result = params.get("result") != null ? params.get("result").toString() : null;
            String feedback = params.get("comment") != null ? params.get("comment").toString() : "";
            
            if ("pass".equals(result)) {
                application.setStatus(4);
            } else if ("fail".equals(result)) {
                application.setStatus(5);
            }
            
            application.setFeedback(feedback);
            application.setUpdateTime(LocalDateTime.now());
            
            jobApplicationMapper.updateById(application);
            
            return Result.success("面试结果更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "更新面试结果失败: " + e.getMessage());
        }
    }

    /**
     * 处理申请状态（通过初筛/拒绝/录用等）
     */
    @PutMapping("/{applicationId}/status")
    public Result<?> updateApplicationStatus(@PathVariable Long applicationId, @RequestBody Map<String, Object> params) {
        try {
            JobApplication application = jobApplicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error(404, "投递记录不存在");
            }
            
            String status = params.get("status") != null ? params.get("status").toString() : null;
            if (status == null) {
                return Result.error("状态不能为空");
            }
            
            int statusCode = 1;
            switch (status) {
                case "pending":
                case "shortlisted":
                    statusCode = 2;
                    break;
                case "interviewed":
                case "interview":
                    statusCode = 3;
                    break;
                case "offered":
                case "hired":
                    statusCode = 4;
                    break;
                case "rejected":
                case "reject":
                    statusCode = 5;
                    break;
                case "cancelled":
                    statusCode = 6;
                    break;
                default:
                    statusCode = Integer.parseInt(status);
                    break;
            }
            
            application.setStatus(statusCode);
            application.setUpdateTime(LocalDateTime.now());
            
            if (params.get("reason") != null) {
                application.setFeedback(params.get("reason").toString());
            }
            
            jobApplicationMapper.updateById(application);
            
            return Result.success("状态更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "更新状态失败: " + e.getMessage());
        }
    }

    /**
     * 企业端处理简历（通过/拒绝）
     */
    @PostMapping("/{applicationId}/process")
    public Result<?> processApplication(@PathVariable Long applicationId, @RequestBody Map<String, Object> params) {
        try {
            JobApplication application = jobApplicationMapper.selectById(applicationId);
            if (application == null) {
                return Result.error(404, "投递记录不存在");
            }
            
            String action = params.get("action") != null ? params.get("action").toString() : null;
            if (action == null) {
                return Result.error("操作类型不能为空");
            }
            
            switch (action) {
                case "pass":
                case "shortlist":
                    application.setStatus(2);
                    break;
                case "reject":
                    application.setStatus(5);
                    break;
                case "interview":
                    application.setStatus(3);
                    break;
                case "offer":
                case "hire":
                    application.setStatus(4);
                    break;
            }
            
            application.setUpdateTime(LocalDateTime.now());
            jobApplicationMapper.updateById(application);
            
            return Result.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "操作失败: " + e.getMessage());
        }
    }

    /**
     * 面试统计数据
     */
    @GetMapping("/interview/stats")
    public Result<?> interviewStats(@RequestParam Long enterpriseId) {
        Map<String, Object> data = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.plusDays(1).atStartOfDay();
        
        QueryWrapper<JobApplication> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("enterprise_id", enterpriseId).eq("status", 3);
        Long pendingCount = jobApplicationMapper.selectCount(pendingWrapper);
        data.put("pendingCount", pendingCount);
        
        QueryWrapper<JobApplication> todayWrapper = new QueryWrapper<>();
        todayWrapper.eq("enterprise_id", enterpriseId).eq("status", 3)
            .ge("interview_time", todayStart).lt("interview_time", todayEnd);
        Long todayCount = jobApplicationMapper.selectCount(todayWrapper);
        data.put("todayCount", todayCount);
        
        LocalDateTime weekStart = today.minusDays(7).atStartOfDay();
        QueryWrapper<JobApplication> weekPassWrapper = new QueryWrapper<>();
        weekPassWrapper.eq("enterprise_id", enterpriseId).eq("status", 4)
            .ge("created_at", weekStart);
        Long weekPassCount = jobApplicationMapper.selectCount(weekPassWrapper);
        data.put("weekPassCount", weekPassCount);
        
        LocalDateTime monthStart = today.withDayOfMonth(1).atStartOfDay();
        QueryWrapper<JobApplication> monthHireWrapper = new QueryWrapper<>();
        monthHireWrapper.eq("enterprise_id", enterpriseId).eq("status", 4)
            .ge("created_at", monthStart);
        Long monthHireCount = jobApplicationMapper.selectCount(monthHireWrapper);
        data.put("monthHireCount", monthHireCount);
        
        return Result.success(data);
    }

    /**
     * 获取企业的职位列表（用于筛选）
     */
    @GetMapping("/jobs")
    public Result<?> getEnterpriseJobs(@RequestParam Long enterpriseId) {
        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        wrapper.eq("enterprise_id", enterpriseId).eq("status", 1).orderByDesc("created_at");
        List<Job> jobs = jobMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Job job : jobs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", job.getId());
            map.put("title", job.getJobTitle());
            result.add(map);
        }

        return Result.success(result);
    }

    /**
     * 教师端投递看板数据
     * 返回：统计数据、趋势图数据、班级排行、专业分布、最近投递
     */
    @GetMapping("/teacher/dashboard")
    public Result<?> teacherDashboard(
            @RequestParam(defaultValue = "30") int period,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Map<String, Object> data = new HashMap<>();

            // 获取所有学生
            List<Student> allStudents = studentMapper.selectList(null);
            int totalStudents = allStudents.size();

            // 构建 studentId -> Student 映射
            Map<Long, Student> studentMap = allStudents.stream()
                    .collect(Collectors.toMap(Student::getId, s -> s, (a, b) -> a));

            List<Long> studentIds = new ArrayList<>(studentMap.keySet());

            // 获取所有投递记录
            List<JobApplication> allApps = Collections.emptyList();
            if (!studentIds.isEmpty()) {
                QueryWrapper<JobApplication> appWrapper = new QueryWrapper<>();
                appWrapper.in("student_id", studentIds);
                allApps = jobApplicationMapper.selectList(appWrapper);
            }

            // ===== 1. 统计卡片 =====
            Set<Long> appliedStudents = new HashSet<>();
            Set<Long> interviewStudents = new HashSet<>();
            Set<Long> hiredStudents = new HashSet<>();

            for (JobApplication app : allApps) {
                appliedStudents.add(app.getStudentId());
                if (app.getStatus() != null && app.getStatus() >= 3) {
                    interviewStudents.add(app.getStudentId());
                }
                if (app.getStatus() != null && app.getStatus() == 4) {
                    hiredStudents.add(app.getStudentId());
                }
            }

            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("totalStudents", totalStudents);
            stats.put("appliedCount", appliedStudents.size());
            stats.put("interviewCount", interviewStudents.size());
            stats.put("hiredCount", hiredStudents.size());
            data.put("stats", stats);

            // ===== 2. 投递趋势（折线图） =====
            LocalDate today = LocalDate.now();
            LocalDate startDate = today.minusDays(period - 1);

            List<String> dates = new ArrayList<>();
            List<Integer> applyTrend = new ArrayList<>();
            List<Integer> interviewTrend = new ArrayList<>();
            List<Integer> hireTrend = new ArrayList<>();

            // 按天分组统计
            Map<LocalDate, Integer> applyByDay = new HashMap<>();
            Map<LocalDate, Integer> interviewByDay = new HashMap<>();
            Map<LocalDate, Integer> hireByDay = new HashMap<>();

            for (JobApplication app : allApps) {
                if (app.getCreatedAt() != null) {
                    LocalDate d = app.getCreatedAt().toLocalDate();
                    if (!d.isBefore(startDate) && !d.isAfter(today)) {
                        applyByDay.merge(d, 1, Integer::sum);
                        if (app.getStatus() != null && app.getStatus() >= 3) {
                            interviewByDay.merge(d, 1, Integer::sum);
                        }
                        if (app.getStatus() != null && app.getStatus() == 4) {
                            hireByDay.merge(d, 1, Integer::sum);
                        }
                    }
                }
            }

            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd");
            for (LocalDate d = startDate; !d.isAfter(today); d = d.plusDays(1)) {
                dates.add(d.format(dateFmt));
                applyTrend.add(applyByDay.getOrDefault(d, 0));
                interviewTrend.add(interviewByDay.getOrDefault(d, 0));
                hireTrend.add(hireByDay.getOrDefault(d, 0));
            }

            Map<String, Object> trend = new LinkedHashMap<>();
            trend.put("dates", dates);
            List<Map<String, Object>> trendSeries = new ArrayList<>();
            trendSeries.add(Map.of("name", "投递数", "data", applyTrend));
            trendSeries.add(Map.of("name", "面试数", "data", interviewTrend));
            trendSeries.add(Map.of("name", "录用数", "data", hireTrend));
            trend.put("series", trendSeries);
            data.put("trend", trend);

            // ===== 3. 班级投递排行 =====
            Map<String, Integer> classAppCount = new LinkedHashMap<>();
            Map<String, Integer> classStudentCount = new LinkedHashMap<>();
            for (Student s : allStudents) {
                String className = (s.getCollege() != null && !s.getCollege().isEmpty()) ? s.getCollege() : "未分班";
                classStudentCount.merge(className, 1, Integer::sum);
            }
            for (JobApplication app : allApps) {
                Student s = studentMap.get(app.getStudentId());
                if (s != null) {
                    String className = (s.getCollege() != null && !s.getCollege().isEmpty()) ? s.getCollege() : "未分班";
                    classAppCount.merge(className, 1, Integer::sum);
                }
            }

            List<Map<String, Object>> classRanking = new ArrayList<>();
            for (String className : classStudentCount.keySet()) {
                int studentCount = classStudentCount.getOrDefault(className, 1);
                int appCount = classAppCount.getOrDefault(className, 0);
                int rate = Math.min(100, (appCount * 100) / Math.max(1, studentCount));
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("name", className);
                item.put("count", appCount);
                item.put("studentCount", studentCount);
                item.put("rate", rate);
                classRanking.add(item);
            }
            classRanking.sort((a, b) -> Integer.compare((int) b.get("count"), (int) a.get("count")));
            data.put("classRanking", classRanking.size() > 8 ? classRanking.subList(0, 8) : classRanking);

            // ===== 4. 专业就业分布（饼图） =====
            Map<String, Integer> majorCount = new LinkedHashMap<>();
            for (Student s : allStudents) {
                String major = (s.getMajor() != null && !s.getMajor().isEmpty()) ? s.getMajor() : "未知专业";
                majorCount.merge(major, 1, Integer::sum);
            }
            List<Map<String, Object>> majorDistribution = new ArrayList<>();
            String[] colors = {"#409EFF", "#67C23A", "#E6A23C", "#F56C6C", "#909399", "#37A2DA", "#32C5E9", "#67E0E3"};
            int ci = 0;
            for (Map.Entry<String, Integer> entry : majorCount.entrySet()) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("name", entry.getKey());
                item.put("value", entry.getValue());
                item.put("itemStyle", Map.of("color", colors[ci % colors.length]));
                majorDistribution.add(item);
                ci++;
            }
            data.put("majorDistribution", majorDistribution);

            // ===== 5. 最近投递动态 =====
            allApps.sort((a, b) -> {
                LocalDateTime ta = a.getCreatedAt() != null ? a.getCreatedAt() : LocalDateTime.MIN;
                LocalDateTime tb = b.getCreatedAt() != null ? b.getCreatedAt() : LocalDateTime.MIN;
                return tb.compareTo(ta);
            });

            List<Map<String, Object>> recentApps = new ArrayList<>();
            int limit = Math.min(20, allApps.size());
            for (int i = 0; i < limit; i++) {
                JobApplication app = allApps.get(i);
                Map<String, Object> item = new LinkedHashMap<>();

                Student student = studentMap.get(app.getStudentId());
                User user = student != null ? userMapper.selectById(student.getUserId()) : null;
                item.put("studentName", user != null && user.getRealName() != null ? user.getRealName() : "未知");

                Job job = app.getJobId() != null ? jobMapper.selectById(app.getJobId()) : null;
                item.put("jobTitle", job != null ? job.getJobTitle() : "未知职位");

                Enterprise ent = job != null && job.getEnterpriseId() != null ? enterpriseMapper.selectById(job.getEnterpriseId()) : null;
                item.put("enterpriseName", ent != null ? ent.getCompanyName() : "未知企业");

                item.put("status", mapCodeToStatus(app.getStatus()));
                item.put("createTime", app.getCreatedAt() != null ? app.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "");
                recentApps.add(item);
            }
            data.put("recentApplications", recentApps);

            return Result.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取看板数据失败: " + e.getMessage());
        }
    }
}
