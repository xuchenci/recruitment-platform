package com.recruitment.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.recruitment.common.entity.ImportHistory;
import com.recruitment.common.entity.Student;
import com.recruitment.common.entity.User;
import com.recruitment.common.mapper.ImportHistoryMapper;
import com.recruitment.common.mapper.StudentMapper;
import com.recruitment.common.mapper.UserMapper;
import com.recruitment.student.dto.ImportErrorDTO;
import com.recruitment.student.dto.ImportResultDTO;
import com.recruitment.student.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 学生服务实现类
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ImportHistoryMapper importHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public Student register(Student student, String username, String password, String phone) {
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getPhone, phone);
        if (userMapper.exists(userQuery)) {
            throw new RuntimeException("该手机号已被注册");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setUserType(1);
        user.setStatus(1);
        userMapper.insert(user);

        student.setUserId(user.getId());
        save(student);

        return student;
    }

    @Override
    public Student updateStudent(Long studentId, Student student) {
        Student existing = getById(studentId);
        if (existing == null) {
            throw new RuntimeException("学生不存在");
        }

        student.setId(studentId);
        updateById(student);

        updateResumeScore(studentId);

        return getById(studentId);
    }

    @Override
    public Map<String, Object> getStudentDetail(Long studentId) {
        Student student = getById(studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("student", student);

        return detail;
    }

    @Override
    public List<Student> searchStudents(String keyword, String major, String degree, Integer graduationYear) {
        LambdaQueryWrapper<Student> query = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            query.like(Student::getStudentNo, keyword);
        }
        if (major != null && !major.isEmpty()) {
            query.eq(Student::getMajor, major);
        }
        if (degree != null && !degree.isEmpty()) {
            query.eq(Student::getDegree, degree);
        }
        if (graduationYear != null) {
            query.eq(Student::getGraduationYear, graduationYear);
        }

        return studentMapper.selectList(query);
    }

    @Override
    public void updateResumeScore(Long studentId) {
        Student student = getById(studentId);
        if (student != null) {
            int score = 0;
            if (student.getCollege() != null && !student.getCollege().isEmpty()) score += 10;
            if (student.getMajor() != null && !student.getMajor().isEmpty()) score += 10;
            if (student.getDegree() != null && !student.getDegree().isEmpty()) score += 10;
            if (student.getGraduationYear() != null) score += 10;
            if (student.getSkills() != null && !student.getSkills().isEmpty()) score += 15;
            if (student.getBio() != null && !student.getBio().isEmpty()) score += 15;
            if (student.getExpectationCity() != null && !student.getExpectationCity().isEmpty()) score += 10;
            if (student.getExpectationPosition() != null && !student.getExpectationPosition().isEmpty()) score += 10;
            if (student.getExpectationSalary() != null) score += 10;

            score = Math.min(score, 100);

            student.setResumeScore(score);
            updateById(student);
        }
    }

    @Override
    public boolean updateJobStatus(Long studentId, Integer jobStatus) {
        Student student = getById(studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        student.setJobStatus(jobStatus);
        return updateById(student);
    }

    @Override
    @Transactional
    public ImportResultDTO importStudents(InputStream inputStream, String fileName, Long operatorId, String operatorName) {
        List<ImportErrorDTO> errors = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndexMap = parseHeader(headerRow);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                try {
                    Object[] parsed = parseStudentRow(row, columnIndexMap, errors, i + 1);
                    if (parsed != null) {
                        Student student = (Student) parsed[0];
                        User user = (User) parsed[1];
                        saveStudentWithUser(student, user);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    errors.add(ImportErrorDTO.builder()
                            .rowNum(i + 1)
                            .field("unknown")
                            .reason("解析错误: " + e.getMessage())
                            .build());
                    failCount++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("文件解析失败: " + e.getMessage());
        }

        saveImportHistory(fileName, successCount + failCount, successCount, failCount, operatorId, operatorName, errors);

        return ImportResultDTO.builder()
                .success(failCount == 0)
                .successCount(successCount)
                .failCount(failCount)
                .errors(errors)
                .build();
    }

    @Override
    public List<ImportHistory> getImportHistory(Long operatorId) {
        LambdaQueryWrapper<ImportHistory> query = new LambdaQueryWrapper<>();
        if (operatorId != null) {
            query.eq(ImportHistory::getOperatorId, operatorId);
        }
        query.eq(ImportHistory::getImportType, "student");
        query.orderByDesc(ImportHistory::getCreatedAt);
        return importHistoryMapper.selectList(query);
    }

    private Map<String, Integer> parseHeader(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String header = getCellStringValue(cell).trim();
                map.put(header, i);
            }
        }
        return map;
    }

    private boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellStringValue(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Object[] parseStudentRow(Row row, Map<String, Integer> columnIndexMap, List<ImportErrorDTO> errors, int rowNum) {
        Student student = new Student();
        User user = new User();

        String studentNo = getCellValue(row, columnIndexMap, "学号");
        if (studentNo == null || studentNo.trim().isEmpty()) {
            errors.add(ImportErrorDTO.builder()
                    .rowNum(rowNum)
                    .field("学号")
                    .reason("学号不能为空")
                    .build());
            return null;
        }
        student.setStudentNo(studentNo.trim());

        String name = getCellValue(row, columnIndexMap, "姓名");
        if (name == null || name.trim().isEmpty()) {
            errors.add(ImportErrorDTO.builder()
                    .rowNum(rowNum)
                    .field("姓名")
                    .reason("姓名不能为空")
                    .build());
            return null;
        }
        user.setRealName(name.trim());

        String phone = getCellValue(row, columnIndexMap, "手机号");
        if (phone == null || phone.trim().isEmpty()) {
            errors.add(ImportErrorDTO.builder()
                    .rowNum(rowNum)
                    .field("手机号")
                    .reason("手机号不能为空")
                    .build());
            return null;
        }
        phone = phone.trim().replaceAll("[^0-9]", "");
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            errors.add(ImportErrorDTO.builder()
                    .rowNum(rowNum)
                    .field("手机号")
                    .reason("手机号格式不正确")
                    .build());
            return null;
        }
        user.setPhone(phone);

        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getPhone, phone);
        if (userMapper.exists(userQuery)) {
            errors.add(ImportErrorDTO.builder()
                    .rowNum(rowNum)
                    .field("手机号")
                    .reason("手机号已被注册")
                    .build());
            return null;
        }

        String email = getCellValue(row, columnIndexMap, "邮箱");
        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email.trim());
        }

        String college = getCellValue(row, columnIndexMap, "学院");
        if (college != null && !college.trim().isEmpty()) {
            student.setCollege(college.trim());
        }

        String major = getCellValue(row, columnIndexMap, "专业");
        if (major != null && !major.trim().isEmpty()) {
            student.setMajor(major.trim());
        }

        String degree = getCellValue(row, columnIndexMap, "学历");
        if (degree != null && !degree.trim().isEmpty()) {
            student.setDegree(degree.trim());
        }

        Integer graduationYear = getCellIntValue(row, columnIndexMap, "毕业年份");
        if (graduationYear != null) {
            student.setGraduationYear(graduationYear);
        }

        String genderStr = getCellValue(row, columnIndexMap, "性别");
        if (genderStr != null && !genderStr.trim().isEmpty()) {
            String gender = genderStr.trim();
            if ("男".equals(gender) || "1".equals(gender)) {
                student.setGender(1);
            } else if ("女".equals(gender) || "2".equals(gender)) {
                student.setGender(2);
            } else {
                student.setGender(0);
            }
        }

        String skills = getCellValue(row, columnIndexMap, "技能");
        if (skills != null && !skills.trim().isEmpty()) {
            student.setSkills(skills.trim());
        }

        String bio = getCellValue(row, columnIndexMap, "个人简介");
        if (bio != null && !bio.trim().isEmpty()) {
            student.setBio(bio.trim());
        }

        String expectationCity = getCellValue(row, columnIndexMap, "期望城市");
        if (expectationCity != null && !expectationCity.trim().isEmpty()) {
            student.setExpectationCity(expectationCity.trim());
        }

        String expectationPosition = getCellValue(row, columnIndexMap, "期望职位");
        if (expectationPosition != null && !expectationPosition.trim().isEmpty()) {
            student.setExpectationPosition(expectationPosition.trim());
        }

        Integer expectationSalary = getCellIntValue(row, columnIndexMap, "期望薪资");
        if (expectationSalary != null) {
            student.setExpectationSalary(expectationSalary);
        }

        student.setJobStatus(1);
        student.setResumeScore(0);

        user.setUsername(studentNo.trim());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setUserType(1);
        user.setStatus(1);

        return new Object[]{student, user};
    }

    private String getCellValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer index = columnIndexMap.get(columnName);
        if (index == null) {
            return null;
        }
        Cell cell = row.getCell(index);
        return getCellStringValue(cell);
    }

    private Integer getCellIntValue(Row row, Map<String, Integer> columnIndexMap, String columnName) {
        Integer index = columnIndexMap.get(columnName);
        if (index == null) {
            return null;
        }
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : Integer.parseInt(value);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return null;
        }
    }

    private void saveStudentWithUser(Student student, User user) {
        userMapper.insert(user);
        student.setUserId(user.getId());
        studentMapper.insert(student);
    }

    private void saveImportHistory(String fileName, int totalCount, int successCount, int failCount,
                                   Long operatorId, String operatorName, List<ImportErrorDTO> errors) {
        ImportHistory history = new ImportHistory();
        history.setFileName(fileName);
        history.setTotalCount(totalCount);
        history.setSuccessCount(successCount);
        history.setFailCount(failCount);
        history.setOperatorId(operatorId);
        history.setOperator(operatorName);
        history.setImportType("student");

        if (!errors.isEmpty()) {
            try {
                history.setErrorDetails(objectMapper.writeValueAsString(errors));
            } catch (JsonProcessingException e) {
                history.setErrorDetails("[]");
            }
        }

        importHistoryMapper.insert(history);
    }
}
