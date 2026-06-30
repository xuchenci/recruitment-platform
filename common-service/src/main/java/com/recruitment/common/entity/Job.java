package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 职位实体类
 */
@Data
@TableName("job")
public class Job extends BaseEntity {
    
    private Long enterpriseId;
    
    private Long categoryId;
    
    private String jobTitle;
    
    /**
     * 工作类型：1-全职，2-实习，3-兼职
     */
    private Integer jobType;
    
    private Integer salaryMin;
    
    private Integer salaryMax;
    
    private String city;
    
    private String district;
    
    private String address;
    
    private String experienceRequirement;
    
    private String educationRequirement;
    
    private String skillRequirements; // JSON格式
    
    private String jobDescription;
    
    private String jobResponsibility;
    
    private String benefits; // JSON格式
    
    /**
     * 状态：0-下架，1-招聘中，2-暂停
     */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime publishTime;
    
    private LocalDateTime expireTime;
    
    private Integer viewCount;
    
    private Integer applyCount;
}
