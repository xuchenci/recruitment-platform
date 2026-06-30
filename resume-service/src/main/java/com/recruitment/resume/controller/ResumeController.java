package com.recruitment.resume.controller;

import com.recruitment.common.entity.Resume;
import com.recruitment.common.entity.Student;
import com.recruitment.common.mapper.ResumeMapper;
import com.recruitment.common.mapper.StudentMapper;
import com.recruitment.common.result.Result;
import com.recruitment.common.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/resume", produces = "application/json;charset=UTF-8")
public class ResumeController {

    @Autowired
    private ResumeMapper resumeMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 获取简历列表
     */
    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Long userId) {
        QueryWrapper<Resume> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("student_id", userId);
        }
        wrapper.orderByDesc("updated_at");
        Page<Resume> pageResult = resumeMapper.selectPage(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    /**
     * 根据ID获取简历详情
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        Resume resume = resumeMapper.selectById(id);
        if (resume != null) {
            // 解析JSON字段返回完整数据
            Map<String, Object> result = parseResumeToJson(resume);
            return Result.success(result);
        }
        return Result.error(404, "简历不存在");
    }

    /**
     * 根据用户ID获取简历
     * 支持"current"参数从JWT令牌中获取当前用户ID
     */
    @GetMapping("/user/{userId}")
    public Result<?> getByUserId(@PathVariable String userId,
                                  @RequestHeader(value = "Authorization", required = false) String token) {
        Long userIdLong = null;
        
        if ("current".equals(userId)) {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    userIdLong = jwtUtil.getUserIdFromToken(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                userIdLong = Long.parseLong(userId);
            } catch (NumberFormatException e) {
                return Result.error(400, "无效的用户ID");
            }
        }
        
        if (userIdLong == null) {
            return Result.error(401, "请登录");
        }
        
        Long studentId = getStudentIdByUserId(userIdLong);
        if (studentId == null) {
            return Result.success(null);
        }
        
        QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
            .eq("student_id", studentId)
            .orderByDesc("file_url is not null")
            .orderByDesc("is_default")
            .last("LIMIT 1");
        Resume resume = resumeMapper.selectOne(wrapper, false);
        
        if (resume != null) {
            Map<String, Object> result = parseResumeToJson(resume);
            
            if (resume.getFileUrl() != null && !resume.getFileUrl().isEmpty()) {
                result.put("fileUrl", resume.getFileUrl());
                result.put("resumeName", resume.getResumeName());
                result.put("previewUrl", "/api/resume/preview/" + resume.getId());

                try {
                    String pdfContent = extractTextFromPDF("." + resume.getFileUrl());
                    if (pdfContent != null && !pdfContent.isEmpty()) {
                        result.put("pdfContent", pdfContent);
                    }
                } catch (Exception e) {
                    System.out.println("[WARN] PDF文本提取失败，跳过: " + e.getMessage());
                }
            }
            
            return Result.success(result);
        }
        return Result.success(null);
    }

    /**
     * 保存简历（新增或更新）
     * 完整度必须达到100%才能保存
     */
    @PostMapping("/save")
    public Result<?> saveResume(@RequestBody Map<String, Object> resumeData,
                                  @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = null;
            
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    userId = jwtUtil.getUserIdFromToken(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (userId == null && resumeData.containsKey("studentId")) {
                userId = Long.valueOf(resumeData.get("studentId").toString());
            }
            
            if (userId == null) {
                return Result.error(401, "请登录");
            }
            
            Long studentId = getStudentIdByUserId(userId);
            
            // 计算完整度
            int completeness = calculateCompleteness(resumeData);
            
            // 完整度校验：必须100%才能保存
            if (completeness < 100) {
                return Result.error(400, "简历完整度不足100%，请完善以下内容：" + getMissingItems(resumeData));
            }
            
            // 查询是否已有简历
            QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
                .eq("student_id", studentId)
                .last("LIMIT 1");
            Resume existingResume = resumeMapper.selectOne(wrapper, false);
            
            Resume resume = existingResume != null ? existingResume : new Resume();
            resume.setStudentId(studentId);
            resume.setCompleteness(completeness);
            resume.setIsDefault(1);
            resume.setIsPublic(1);
            resume.setViewCount(existingResume != null ? existingResume.getViewCount() : 0);
            resume.setDownloadCount(existingResume != null ? existingResume.getDownloadCount() : 0);
            
            // 设置基本信息
            Map<String, Object> basic = (Map<String, Object>) resumeData.get("basic");
            if (basic != null) {
                resume.setRealName((String) basic.get("realName"));
                resume.setGender((String) basic.get("gender"));
                resume.setBirthDate((String) basic.get("birthDate"));
                resume.setPhone((String) basic.get("phone"));
                resume.setEmail((String) basic.get("email"));
                resume.setCity((String) basic.get("city"));
                resume.setSummary((String) basic.get("summary"));
            }
            
            // 设置求职意向
            Map<String, Object> intention = (Map<String, Object>) resumeData.get("intention");
            if (intention != null) {
                resume.setExpectedPosition((String) intention.get("expectedPosition"));
                resume.setExpectedCity((String) intention.get("expectedCity"));
                resume.setExpectedSalary((String) intention.get("expectedSalary"));
            }
            
            // 设置JSON字段
            resume.setEducations(objectMapper.writeValueAsString(resumeData.get("educations")));
            resume.setExperiences(objectMapper.writeValueAsString(resumeData.get("experiences")));
            resume.setProjects(objectMapper.writeValueAsString(resumeData.get("projects")));
            resume.setSkills(objectMapper.writeValueAsString(resumeData.get("skills")));
            resume.setLanguages(objectMapper.writeValueAsString(resumeData.get("languages")));
            resume.setCertificates(objectMapper.writeValueAsString(resumeData.get("certificates")));
            
            resume.setResumeName(resume.getRealName() + "的简历");
            resume.setUpdatedAt(LocalDateTime.now());
            
            if (existingResume != null) {
                resumeMapper.updateById(resume);
                return Result.success("简历更新成功");
            } else {
                resume.setCreatedAt(LocalDateTime.now());
                resumeMapper.insert(resume);
                return Result.success("简历保存成功");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "保存失败：" + e.getMessage());
        }
    }

    /**
     * 修改简历（无需完整度100%限制）
     */
    @PostMapping("/update")
    public Result<?> updateResume(@RequestBody Map<String, Object> resumeData,
                                   @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            Long userId = null;
            
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                try {
                    userId = jwtUtil.getUserIdFromToken(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            if (userId == null && resumeData.containsKey("studentId")) {
                userId = Long.valueOf(resumeData.get("studentId").toString());
            }
            
            if (userId == null) {
                return Result.error(401, "请登录");
            }
            
            Long studentId = getStudentIdByUserId(userId);
            
            QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
                .eq("student_id", studentId)
                .last("LIMIT 1");
            Resume existingResume = resumeMapper.selectOne(wrapper, false);
            
            if (existingResume == null) {
                return Result.error(400, "请先保存简历");
            }
            
            Resume resume = existingResume;
            
            // 设置基本信息
            Map<String, Object> basic = (Map<String, Object>) resumeData.get("basic");
            if (basic != null) {
                if (basic.containsKey("realName")) resume.setRealName((String) basic.get("realName"));
                if (basic.containsKey("gender")) resume.setGender((String) basic.get("gender"));
                if (basic.containsKey("birthDate")) resume.setBirthDate((String) basic.get("birthDate"));
                if (basic.containsKey("phone")) resume.setPhone((String) basic.get("phone"));
                if (basic.containsKey("email")) resume.setEmail((String) basic.get("email"));
                if (basic.containsKey("city")) resume.setCity((String) basic.get("city"));
                if (basic.containsKey("summary")) resume.setSummary((String) basic.get("summary"));
            }
            
            // 设置求职意向
            Map<String, Object> intention = (Map<String, Object>) resumeData.get("intention");
            if (intention != null) {
                if (intention.containsKey("expectedPosition")) resume.setExpectedPosition((String) intention.get("expectedPosition"));
                if (intention.containsKey("expectedCity")) resume.setExpectedCity((String) intention.get("expectedCity"));
                if (intention.containsKey("expectedSalary")) resume.setExpectedSalary((String) intention.get("expectedSalary"));
            }
            
            // 设置JSON字段（仅更新非空字段）
            if (resumeData.containsKey("educations")) {
                resume.setEducations(objectMapper.writeValueAsString(resumeData.get("educations")));
            }
            if (resumeData.containsKey("experiences")) {
                resume.setExperiences(objectMapper.writeValueAsString(resumeData.get("experiences")));
            }
            if (resumeData.containsKey("projects")) {
                resume.setProjects(objectMapper.writeValueAsString(resumeData.get("projects")));
            }
            if (resumeData.containsKey("skills")) {
                resume.setSkills(objectMapper.writeValueAsString(resumeData.get("skills")));
            }
            if (resumeData.containsKey("languages")) {
                resume.setLanguages(objectMapper.writeValueAsString(resumeData.get("languages")));
            }
            if (resumeData.containsKey("certificates")) {
                resume.setCertificates(objectMapper.writeValueAsString(resumeData.get("certificates")));
            }
            
            // 更新完整度
            resume.setCompleteness(calculateCompleteness(resumeData));
            
            if (resume.getRealName() != null) {
                resume.setResumeName(resume.getRealName() + "的简历");
            }
            resume.setUpdatedAt(LocalDateTime.now());
            
            resumeMapper.updateById(resume);
            return Result.success("简历修改成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "修改失败：" + e.getMessage());
        }
    }

    /**
     * 根据userId获取studentId
     */
    private Long getStudentIdByUserId(Long userId) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getUserId, userId);
        Student student = studentMapper.selectOne(wrapper);
        if (student != null) {
            return student.getId();
        }
        return null;
    }

    /**
     * 计算简历完整度
     */
    private int calculateCompleteness(Map<String, Object> resumeData) {
        int totalItems = 5;
        int completedItems = 0;
        
        // 1. 基本信息（姓名+手机）
        Map<String, Object> basic = (Map<String, Object>) resumeData.get("basic");
        if (basic != null && 
            basic.get("realName") != null && !basic.get("realName").toString().isEmpty() &&
            basic.get("phone") != null && !basic.get("phone").toString().isEmpty()) {
            completedItems++;
        }
        
        // 2. 教育经历
        List<Object> educations = (List<Object>) resumeData.get("educations");
        if (educations != null && educations.size() > 0) {
            completedItems++;
        }
        
        // 3. 工作经历
        List<Object> experiences = (List<Object>) resumeData.get("experiences");
        if (experiences != null && experiences.size() > 0) {
            completedItems++;
        }
        
        // 4. 专业技能
        List<Object> skills = (List<Object>) resumeData.get("skills");
        if (skills != null && skills.size() > 0) {
            completedItems++;
        }
        
        // 5. 求职意向
        Map<String, Object> intention = (Map<String, Object>) resumeData.get("intention");
        if (intention != null && 
            intention.get("expectedPosition") != null && !intention.get("expectedPosition").toString().isEmpty()) {
            completedItems++;
        }
        
        return (completedItems * 100) / totalItems;
    }

    /**
     * 获取缺失项提示
     */
    private String getMissingItems(Map<String, Object> resumeData) {
        StringBuilder missing = new StringBuilder();
        
        Map<String, Object> basic = (Map<String, Object>) resumeData.get("basic");
        if (basic == null || 
            basic.get("realName") == null || basic.get("realName").toString().isEmpty() ||
            basic.get("phone") == null || basic.get("phone").toString().isEmpty()) {
            missing.append("基本信息（姓名、手机）");
        }
        
        List<Object> educations = (List<Object>) resumeData.get("educations");
        if (educations == null || educations.size() == 0) {
            if (missing.length() > 0) missing.append("、");
            missing.append("教育经历");
        }
        
        List<Object> experiences = (List<Object>) resumeData.get("experiences");
        if (experiences == null || experiences.size() == 0) {
            if (missing.length() > 0) missing.append("、");
            missing.append("工作经历");
        }
        
        List<Object> skills = (List<Object>) resumeData.get("skills");
        if (skills == null || skills.size() == 0) {
            if (missing.length() > 0) missing.append("、");
            missing.append("专业技能");
        }
        
        Map<String, Object> intention = (Map<String, Object>) resumeData.get("intention");
        if (intention == null || 
            intention.get("expectedPosition") == null || intention.get("expectedPosition").toString().isEmpty()) {
            if (missing.length() > 0) missing.append("、");
            missing.append("求职意向");
        }
        
        return missing.toString();
    }

    /**
     * 解析简历JSON字段为完整数据结构
     */
    private Map<String, Object> parseResumeToJson(Resume resume) {
        Map<String, Object> result = new HashMap<>();
        
        // 基本信息
        Map<String, Object> basic = new HashMap<>();
        basic.put("realName", resume.getRealName());
        basic.put("gender", resume.getGender());
        basic.put("birthDate", resume.getBirthDate());
        basic.put("phone", resume.getPhone());
        basic.put("email", resume.getEmail());
        basic.put("city", resume.getCity());
        basic.put("summary", resume.getSummary());
        result.put("basic", basic);
        
        // 求职意向
        Map<String, Object> intention = new HashMap<>();
        intention.put("expectedPosition", resume.getExpectedPosition());
        intention.put("expectedCity", resume.getExpectedCity());
        intention.put("expectedSalary", resume.getExpectedSalary());
        result.put("intention", intention);
        
        // 解析JSON列表字段
        try {
            result.put("educations", objectMapper.readValue(resume.getEducations() != null ? resume.getEducations() : "[]", List.class));
            result.put("experiences", objectMapper.readValue(resume.getExperiences() != null ? resume.getExperiences() : "[]", List.class));
            result.put("projects", objectMapper.readValue(resume.getProjects() != null ? resume.getProjects() : "[]", List.class));
            result.put("skills", objectMapper.readValue(resume.getSkills() != null ? resume.getSkills() : "[]", List.class));
            result.put("languages", objectMapper.readValue(resume.getLanguages() != null ? resume.getLanguages() : "[]", List.class));
            result.put("certificates", objectMapper.readValue(resume.getCertificates() != null ? resume.getCertificates() : "[]", List.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        result.put("id", resume.getId());
        result.put("studentId", resume.getStudentId());
        result.put("completeness", resume.getCompleteness());
        result.put("isPublic", resume.getIsPublic());
        result.put("viewCount", resume.getViewCount());
        result.put("downloadCount", resume.getDownloadCount());
        
        return result;
    }

    /**
     * 删除简历
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        resumeMapper.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 增加简历浏览次数
     */
    @PostMapping("/{id}/view")
    public Result<?> incrementViewCount(@PathVariable Long id) {
        Resume resume = resumeMapper.selectById(id);
        if (resume != null) {
            resume.setViewCount(resume.getViewCount() + 1);
            resumeMapper.updateById(resume);
            return Result.success("浏览次数+1");
        }
        return Result.error(404, "简历不存在");
    }

    /**
     * 增加简历下载次数
     */
    @PostMapping("/{id}/download")
    public Result<?> incrementDownloadCount(@PathVariable Long id) {
        Resume resume = resumeMapper.selectById(id);
        if (resume != null) {
            resume.setDownloadCount(resume.getDownloadCount() + 1);
            resumeMapper.updateById(resume);
            return Result.success("下载次数+1");
        }
        return Result.error(404, "简历不存在");
    }

    /**
     * 上传PDF简历文件
     */
    @PostMapping("/upload")
    public Result<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        if (file.isEmpty()) {
            return Result.error(400, "请选择要上传的文件");
        }
        
        String filename = file.getOriginalFilename();
        if (!filename.toLowerCase().endsWith(".pdf")) {
            return Result.error(400, "只支持PDF格式的简历文件");
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
            return Result.error(401, "请登录");
        }
        
        Long studentId = getStudentIdByUserId(userId);
        if (studentId == null) {
            return Result.error(400, "用户不存在");
        }
        
        try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/resumes/";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String extension = filename.substring(filename.lastIndexOf("."));
                String newFilename = uuid + extension;
                String filePath = uploadDir + newFilename;
                
                file.transferTo(new File(filePath));
                
                String fileUrl = "/uploads/resumes/" + newFilename;
            
            String pdfContent = extractTextFromPDF(filePath);
            
            QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
                    .eq("student_id", studentId)
                    .last("LIMIT 1");
            Resume existingResume = resumeMapper.selectOne(wrapper, false);
            
            Resume resume = existingResume != null ? existingResume : new Resume();
            resume.setStudentId(studentId);
            resume.setFileUrl(fileUrl);
            resume.setResumeName(filename);
            resume.setUpdatedAt(LocalDateTime.now());
            
            if (pdfContent != null && !pdfContent.isEmpty()) {
                resume.setSummary(pdfContent);
            }
            
            if (existingResume != null) {
                resumeMapper.updateById(resume);
            } else {
                resume.setCreatedAt(LocalDateTime.now());
                resume.setIsDefault(1);
                resume.setIsPublic(1);
                resume.setCompleteness(0);
                resume.setViewCount(0);
                resume.setDownloadCount(0);
                resumeMapper.insert(resume);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", resume.getId());
            result.put("fileUrl", fileUrl);
            result.put("fileName", filename);
            result.put("pdfContent", pdfContent);
            
            return Result.success(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "上传失败：" + e.getMessage());
        }
    }

    /**
     * 从PDF文件中提取文本内容
     */
    private String extractTextFromPDF(String filePath) {
        File pdfFile = new File(filePath);
        if (!pdfFile.exists()) {
            System.out.println("[WARN] PDF文件不存在: " + filePath);
            return "";
        }
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            System.out.println("[WARN] PDF解析失败: " + filePath + " - " + e.getMessage());
            return "";
        }
    }

    /**
     * 获取上传的简历文件
     */
    @GetMapping("/file/{filename}")
    public Result<?> getResumeFile(@PathVariable String filename) {
        String filePath = "uploads/resumes/" + filename;
        File file = new File(filePath);
        
        if (!file.exists()) {
            return Result.error(404, "文件不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("filePath", filePath);
        result.put("fileName", filename);
        
        return Result.success(result);
    }

    /**
     * 预览PDF简历文件（直接返回文件流）
     */
    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> previewResume(@PathVariable Long id) {
        Resume resume = resumeMapper.selectById(id);
        
        if (resume == null || resume.getFileUrl() == null || resume.getFileUrl().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        String filename = resume.getFileUrl().substring(resume.getFileUrl().lastIndexOf("/") + 1);
        String filePath = System.getProperty("user.dir") + "/uploads/resumes/" + filename;
        File file = new File(filePath);
        
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resume.getResumeName() + "\"")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(resource);
    }
}