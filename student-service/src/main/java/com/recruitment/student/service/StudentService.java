package com.recruitment.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.recruitment.common.entity.ImportHistory;
import com.recruitment.common.entity.Student;
import com.recruitment.student.dto.ImportResultDTO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 学生服务接口
 */
public interface StudentService extends IService<Student> {
    
    /**
     * 注册学生账号
     */
    Student register(Student student, String username, String password, String phone);
    
    /**
     * 更新学生信息
     */
    Student updateStudent(Long studentId, Student student);
    
    /**
     * 获取学生详细信息（包含用户信息和学校信息）
     */
    Map<String, Object> getStudentDetail(Long studentId);
    
    /**
     * 根据条件查询学生
     */
    List<Student> searchStudents(String keyword, String major, String degree, Integer graduationYear);
    
    /**
     * 更新简历完整度评分
     */
    void updateResumeScore(Long studentId);
    
    /**
     * 更新求职状态
     */
    boolean updateJobStatus(Long studentId, Integer jobStatus);

    /**
     * 导入学生信息
     */
    ImportResultDTO importStudents(InputStream inputStream, String fileName, Long operatorId, String operatorName);

    /**
     * 获取导入历史记录
     */
    List<ImportHistory> getImportHistory(Long operatorId);
}
