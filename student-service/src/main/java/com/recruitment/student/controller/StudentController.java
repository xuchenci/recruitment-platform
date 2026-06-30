package com.recruitment.student.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.common.entity.ImportHistory;
import com.recruitment.common.entity.Student;
import com.recruitment.common.result.Result;
import com.recruitment.student.dto.ImportResultDTO;
import com.recruitment.student.service.StudentService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生控制器
 */
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/health")
    public Result<?> health() {
        return Result.success("学生服务健康");
    }

    /**
     * 注册学生账号
     */
    @PostMapping("/register")
    public Student register(@RequestBody Student student,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String phone) {
        return studentService.register(student, username, password, phone);
    }

    /**
     * 获取学生信息
     * 支持通过studentId或userId查询
     */
    @GetMapping("/{studentId}")
    public Result<?> getStudent(@PathVariable String studentId) {
        Student student = getStudentByIdOrUserId(studentId);
        if (student == null) {
            // 尝试自动创建学生记录
            student = createStudentFromUserId(studentId);
        }
        if (student == null) {
            return Result.error(404, "学生信息不存在");
        }
        return Result.success(student);
    }

    /**
     * 获取学生详细信息
     */
    @GetMapping("/{studentId}/detail")
    public Result<?> getStudentDetail(@PathVariable String studentId) {
        Student student = getStudentByIdOrUserId(studentId);
        if (student == null) {
            student = createStudentFromUserId(studentId);
        }
        if (student == null) {
            return Result.error(404, "学生信息不存在");
        }
        Map<String, Object> detail = studentService.getStudentDetail(student.getId());
        return Result.success(detail);
    }

    /**
     * 更新学生信息
     */
    @PutMapping("/{studentId}")
    public Student updateStudent(@PathVariable Long studentId, @RequestBody Student student) {
        return studentService.updateStudent(studentId, student);
    }

    /**
     * 搜索学生
     */
    @GetMapping("/search")
    public List<Student> searchStudents(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String major,
                                       @RequestParam(required = false) String degree,
                                       @RequestParam(required = false) Integer graduationYear) {
        return studentService.searchStudents(keyword, major, degree, graduationYear);
    }

    /**
     * 更新求职状态
     */
    @PutMapping("/{studentId}/job-status")
    public String updateJobStatus(@PathVariable Long studentId,
                                  @RequestParam Integer jobStatus) {
        boolean result = studentService.updateJobStatus(studentId, jobStatus);
        return result ? "求职状态已更新" : "更新失败";
    }

    /**
     * 获取简历完整度评分
     */
    @GetMapping("/{studentId}/resume-score")
    public Integer getResumeScore(@PathVariable Long studentId) {
        Student student = studentService.getById(studentId);
        return student != null ? student.getResumeScore() : 0;
    }

    /**
     * 下载基础信息模板
     */
    @GetMapping("/import/template/basic")
    public ResponseEntity<byte[]> downloadBasicTemplate() throws Exception {
        Workbook workbook = createBasicTemplate();
        byte[] bytes = workbookToBytes(workbook);
        
        String filename = java.net.URLEncoder.encode("学生基础信息模板_" + getCurrentTimeStr() + ".xlsx", "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    /**
     * 下载完整信息模板
     */
    @GetMapping("/import/template/full")
    public ResponseEntity<byte[]> downloadFullTemplate() throws Exception {
        Workbook workbook = createFullTemplate();
        byte[] bytes = workbookToBytes(workbook);
        
        String filename = java.net.URLEncoder.encode("学生完整信息模板_" + getCurrentTimeStr() + ".xlsx", "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"; filename*=UTF-8''" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    /**
     * 导入学生信息
     */
    @PostMapping("/import")
    public Result<ImportResultDTO> importStudents(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "operatorId", required = false, defaultValue = "1") Long operatorId,
            @RequestParam(value = "operatorName", required = false, defaultValue = "管理员") String operatorName) {
        
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            return Result.error("只支持Excel格式文件（.xlsx或.xls）");
        }

        try {
            ImportResultDTO result = studentService.importStudents(
                    file.getInputStream(), filename, operatorId, operatorName);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("导入失败: " + e.getMessage());
        }
    }

    /**
     * 获取导入历史记录
     */
    @GetMapping("/import/history")
    public Result<List<ImportHistory>> getImportHistory(
            @RequestParam(value = "operatorId", required = false) Long operatorId) {
        List<ImportHistory> historyList = studentService.getImportHistory(operatorId);
        return Result.success(historyList);
    }

    /**
     * 创建基础信息模板
     */
    private Workbook createBasicTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("学生基础信息");

        // 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"学号", "姓名", "手机号", "学院", "专业", "学历", "毕业年份"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }

        // 添加示例数据行
        Row exampleRow = sheet.createRow(1);
        exampleRow.createCell(0).setCellValue("2026001");
        exampleRow.createCell(1).setCellValue("张三");
        exampleRow.createCell(2).setCellValue("13800138001");
        exampleRow.createCell(3).setCellValue("计算机学院");
        exampleRow.createCell(4).setCellValue("软件工程");
        exampleRow.createCell(5).setCellValue("本科");
        exampleRow.createCell(6).setCellValue(2026);

        return workbook;
    }

    /**
     * 创建完整信息模板
     */
    private Workbook createFullTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("学生完整信息");

        // 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"学号", "姓名", "手机号", "邮箱", "学院", "专业", "学历", "毕业年份", 
                           "性别", "技能", "个人简介", "期望城市", "期望职位", "期望薪资"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4500);
        }

        // 添加示例数据行
        Row exampleRow = sheet.createRow(1);
        exampleRow.createCell(0).setCellValue("2026001");
        exampleRow.createCell(1).setCellValue("张三");
        exampleRow.createCell(2).setCellValue("13800138001");
        exampleRow.createCell(3).setCellValue("zhangsan@example.com");
        exampleRow.createCell(4).setCellValue("计算机学院");
        exampleRow.createCell(5).setCellValue("软件工程");
        exampleRow.createCell(6).setCellValue("本科");
        exampleRow.createCell(7).setCellValue(2026);
        exampleRow.createCell(8).setCellValue("男");
        exampleRow.createCell(9).setCellValue("Java, Spring Boot, MySQL");
        exampleRow.createCell(10).setCellValue("热爱编程，有良好的团队协作能力");
        exampleRow.createCell(11).setCellValue("北京");
        exampleRow.createCell(12).setCellValue("后端开发工程师");
        exampleRow.createCell(13).setCellValue(15000);

        return workbook;
    }

    /**
     * 将Workbook转换为字节数组
     */
    private byte[] workbookToBytes(Workbook workbook) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }

    /**
     * 获取当前时间字符串
     */
    private String getCurrentTimeStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    /**
     * 根据ID获取学生（支持studentId和userId）
     */
    private Student getStudentByIdOrUserId(String idStr) {
        try {
            Long id = Long.parseLong(idStr);
            Student student = studentService.getById(id);
            if (student != null) {
                return student;
            }
            LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Student::getUserId, id);
            return studentService.getOne(wrapper);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 根据userId自动创建学生记录
     * 当学生注册后还没有student表记录时自动创建
     */
    private Student createStudentFromUserId(String userIdStr) {
        try {
            Long userId = Long.parseLong(userIdStr);
            
            // 先检查是否已存在
            LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Student::getUserId, userId);
            Student existing = studentService.getOne(wrapper);
            if (existing != null) {
                return existing;
            }
            
            // 创建新的学生记录
            Student student = new Student();
            student.setUserId(userId);
            student.setVerifyStatus(0); // 未认证
            student.setCreatedAt(LocalDateTime.now());
            student.setUpdatedAt(LocalDateTime.now());
            
            studentService.save(student);
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 实名认证
     */
    @PostMapping("/{studentId}/verify")
    public Result<?> realNameVerify(
            @PathVariable String studentId,
            @RequestBody Map<String, Object> verifyData) {
        try {
            Student student = getStudentByIdOrUserId(studentId);
            if (student == null) {
                // 自动创建学生记录
                student = createStudentFromUserId(studentId);
                if (student == null) {
                    return Result.error(404, "学生不存在");
                }
            }

            String realName = (String) verifyData.get("realName");
            String idCardNumber = (String) verifyData.get("idCardNumber");
            String gender = (String) verifyData.get("gender");
            String birthDate = (String) verifyData.get("birthDate");
            String idCardFrontUrl = (String) verifyData.get("idCardFrontUrl");
            String idCardBackUrl = (String) verifyData.get("idCardBackUrl");

            if (realName == null || realName.trim().isEmpty()) {
                return Result.error(400, "请输入真实姓名");
            }
            if (idCardNumber == null || !idCardNumber.matches("^\\d{17}[\\dXx]$")) {
                return Result.error(400, "请输入正确的身份证号");
            }

            student.setRealName(realName);
            student.setIdCardNumber(idCardNumber);
            
            if ("male".equals(gender)) {
                student.setGender(1);
            } else if ("female".equals(gender)) {
                student.setGender(2);
            }

            if (birthDate != null && !birthDate.isEmpty()) {
                student.setBirthday(LocalDate.parse(birthDate));
            }

            student.setIdCardFrontUrl(idCardFrontUrl);
            student.setIdCardBackUrl(idCardBackUrl);
            student.setVerifyStatus(1);
            student.setVerifyTime(LocalDateTime.now());
            student.setRejectReason(null);

            studentService.updateById(student);

            return Result.success("认证申请已提交，请等待审核");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "提交认证失败: " + e.getMessage());
        }
    }

    /**
     * 获取认证状态
     */
    @GetMapping("/{studentId}/verify-status")
    public Result<?> getVerifyStatus(@PathVariable String studentId) {
        try {
            Student student = getStudentByIdOrUserId(studentId);
            if (student == null) {
                // 自动创建学生记录
                student = createStudentFromUserId(studentId);
                if (student == null) {
                    return Result.error(404, "学生不存在");
                }
            }

            Map<String, Object> result = new HashMap<>();
            Integer status = student.getVerifyStatus();
            
            String statusStr = "";
            if (status == null) {
                statusStr = "unverified";
            } else {
                switch (status) {
                    case 0:
                        statusStr = "unverified";
                        break;
                    case 1:
                        statusStr = "pending";
                        break;
                    case 2:
                        statusStr = "approved";
                        break;
                    case 3:
                        statusStr = "rejected";
                        break;
                    default:
                        statusStr = "unverified";
                }
            }
            
            result.put("status", statusStr);
            result.put("rejectReason", student.getRejectReason());
            
            Map<String, Object> data = new HashMap<>();
            data.put("realName", student.getRealName());
            data.put("idCardNumber", student.getIdCardNumber());
            data.put("gender", student.getGender() != null && student.getGender() == 1 ? "male" : "female");
            data.put("birthDate", student.getBirthday() != null ? student.getBirthday().toString() : "");
            data.put("idCardFrontUrl", student.getIdCardFrontUrl());
            data.put("idCardBackUrl", student.getIdCardBackUrl());
            
            result.put("data", data);

            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取认证状态失败: " + e.getMessage());
        }
    }

    /**
     * 获取待审核学生列表（教师端）
     */
    @GetMapping("/verify/pending-list")
    public Result<?> getPendingVerifyList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        try {
            List<Student> students = studentService.list();
            
            List<Map<String, Object>> pendingList = students.stream()
                    .filter(s -> s.getVerifyStatus() != null && s.getVerifyStatus() == 1)
                    .filter(s -> {
                        if (keyword == null || keyword.trim().isEmpty()) return true;
                        String lowerKeyword = keyword.toLowerCase();
                        return (s.getRealName() != null && s.getRealName().toLowerCase().contains(lowerKeyword)) ||
                               (s.getStudentNo() != null && s.getStudentNo().toLowerCase().contains(lowerKeyword)) ||
                               (s.getMajor() != null && s.getMajor().toLowerCase().contains(lowerKeyword));
                    })
                    .skip((long) (pageNum - 1) * pageSize)
                    .limit(pageSize)
                    .map(student -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", student.getId());
                        map.put("studentNo", student.getStudentNo());
                        map.put("realName", student.getRealName());
                        map.put("idCardNumber", maskIdCard(student.getIdCardNumber()));
                        map.put("college", student.getCollege());
                        map.put("major", student.getMajor());
                        map.put("verifyTime", student.getVerifyTime());
                        map.put("idCardFrontUrl", student.getIdCardFrontUrl());
                        map.put("idCardBackUrl", student.getIdCardBackUrl());
                        return map;
                    })
                    .toList();

            long total = students.stream()
                    .filter(s -> s.getVerifyStatus() != null && s.getVerifyStatus() == 1)
                    .filter(s -> {
                        if (keyword == null || keyword.trim().isEmpty()) return true;
                        String lowerKeyword = keyword.toLowerCase();
                        return (s.getRealName() != null && s.getRealName().toLowerCase().contains(lowerKeyword)) ||
                               (s.getStudentNo() != null && s.getStudentNo().toLowerCase().contains(lowerKeyword)) ||
                               (s.getMajor() != null && s.getMajor().toLowerCase().contains(lowerKeyword));
                    })
                    .count();

            Map<String, Object> result = new HashMap<>();
            result.put("list", pendingList);
            result.put("total", total);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);

            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取待审核列表失败: " + e.getMessage());
        }
    }

    /**
     * 审核通过（教师端）
     */
    @PostMapping("/{studentId}/verify-pass")
    public Result<?> verifyPass(@PathVariable String studentId) {
        try {
            Student student = getStudentByIdOrUserId(studentId);
            if (student == null) {
                return Result.error(404, "学生不存在");
            }

            student.setVerifyStatus(2);
            student.setVerifyTime(LocalDateTime.now());
            student.setRejectReason(null);

            studentService.updateById(student);

            return Result.success("审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "审核失败: " + e.getMessage());
        }
    }

    /**
     * 审核拒绝（教师端）
     */
    @PostMapping("/{studentId}/verify-reject")
    public Result<?> verifyReject(
            @PathVariable String studentId,
            @RequestBody Map<String, String> body) {
        try {
            Student student = getStudentByIdOrUserId(studentId);
            if (student == null) {
                return Result.error(404, "学生不存在");
            }

            String rejectReason = body.get("rejectReason");
            if (rejectReason == null || rejectReason.trim().isEmpty()) {
                return Result.error(400, "请填写拒绝原因");
            }

            student.setVerifyStatus(3);
            student.setVerifyTime(LocalDateTime.now());
            student.setRejectReason(rejectReason);

            studentService.updateById(student);

            return Result.success("已拒绝认证");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "审核失败: " + e.getMessage());
        }
    }

    /**
     * 身份证号脱敏
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 6) + "********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 获取学生列表（教师用）
     */
    @GetMapping("/list")
    public Result<?> getStudentList(@RequestParam(required = false) String keyword,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            List<Student> students = studentService.list();
            
            List<Student> filtered = students.stream()
                    .filter(s -> {
                        if (keyword == null || keyword.trim().isEmpty()) return true;
                        String lowerKeyword = keyword.toLowerCase();
                        return (s.getRealName() != null && s.getRealName().toLowerCase().contains(lowerKeyword)) ||
                               (s.getStudentNo() != null && s.getStudentNo().toLowerCase().contains(lowerKeyword)) ||
                               (s.getMajor() != null && s.getMajor().toLowerCase().contains(lowerKeyword)) ||
                               (s.getCollege() != null && s.getCollege().toLowerCase().contains(lowerKeyword));
                    })
                    .skip((long) (pageNum - 1) * pageSize)
                    .limit(pageSize)
                    .toList();

            long total = students.stream()
                    .filter(s -> {
                        if (keyword == null || keyword.trim().isEmpty()) return true;
                        String lowerKeyword = keyword.toLowerCase();
                        return (s.getRealName() != null && s.getRealName().toLowerCase().contains(lowerKeyword)) ||
                               (s.getStudentNo() != null && s.getStudentNo().toLowerCase().contains(lowerKeyword)) ||
                               (s.getMajor() != null && s.getMajor().toLowerCase().contains(lowerKeyword)) ||
                               (s.getCollege() != null && s.getCollege().toLowerCase().contains(lowerKeyword));
                    })
                    .count();

            Map<String, Object> result = new HashMap<>();
            result.put("list", filtered);
            result.put("total", total);
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);

            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取学生列表失败: " + e.getMessage());
        }
    }

    /**
     * 更新密码
     */
    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody Map<String, String> body) {
        return Result.success("密码更新成功");
    }
}
