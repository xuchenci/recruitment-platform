package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@Data
@TableName("student")
public class Student extends BaseEntity {
    
    private Long userId;
    
    private String studentNo;
    
    private Long schoolId;
    
    private String college;
    
    private String major;
    
    private String degree;
    
    private Integer enrollmentYear;
    
    private Integer graduationYear;
    
    private Double gpa;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;
    
    private LocalDate birthday;
    
    private String bio;
    
    private String skills; // JSON格式
    
    private Integer expectationSalary;
    
    private String expectationCity;
    
    private String expectationIndustry;
    
    private String expectationPosition;
    
    private Integer resumeScore;
    
    /**
     * 求职状态：1-积极求职，2-观望机会，3-不考虑
     */
    private Integer jobStatus;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号
     */
    private String idCardNumber;
    
    /**
     * 认证状态：0-未认证，1-审核中，2-已通过，3-未通过
     */
    private Integer verifyStatus;
    
    /**
     * 认证时间
     */
    private LocalDateTime verifyTime;
    
    /**
     * 拒绝原因
     */
    private String rejectReason;
    
    /**
     * 身份证正面照片URL
     */
    private String idCardFrontUrl;
    
    /**
     * 身份证反面照片URL
     */
    private String idCardBackUrl;
}
