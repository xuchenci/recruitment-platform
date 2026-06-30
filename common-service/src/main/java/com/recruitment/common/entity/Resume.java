package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 简历实体类 - 包含完整简历信息
 */
@Data
@TableName("resume")
public class Resume extends BaseEntity {
    
    private Long studentId;
    
    private String resumeName;
    
    /**
     * 是否默认简历：0-否，1-是
     */
    private Integer isDefault;
    
    /**
     * 是否公开：0-否，1-是
     */
    private Integer isPublic;
    
    private Integer completeness;
    
    private Integer viewCount;
    
    private Integer downloadCount;
    
    private String fileUrl;
    
    // ==================== 基本信息 ====================
    private String realName;
    private String gender;
    private String birthDate;
    private String phone;
    private String email;
    private String city;
    private String summary;
    
    // ==================== 求职意向 ====================
    private String expectedPosition;
    private String expectedCity;
    private String expectedSalary;
    
    // ==================== 教育经历（JSON） ====================
    /**
     * JSON格式存储教育经历列表
     * 格式: [{"school":"xxx","major":"xxx","degree":"xxx","startDate":"xxx","endDate":"xxx","description":"xxx"}]
     */
    private String educations;
    
    // ==================== 工作经历（JSON） ====================
    /**
     * JSON格式存储工作经历列表
     */
    private String experiences;
    
    // ==================== 项目经验（JSON） ====================
    /**
     * JSON格式存储项目经验列表
     */
    private String projects;
    
    // ==================== 专业技能（JSON） ====================
    /**
     * JSON格式存储技能列表
     */
    private String skills;
    
    // ==================== 语言能力（JSON） ====================
    /**
     * JSON格式存储语言能力列表
     */
    private String languages;
    
    // ==================== 证书荣誉（JSON） ====================
    /**
     * JSON格式存储证书列表
     */
    private String certificates;
}