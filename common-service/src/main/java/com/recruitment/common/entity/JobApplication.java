package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 职位申请实体类
 */
@Data
@TableName("job_application")
public class JobApplication extends BaseEntity {
    
    private Long jobId;
    
    private Long studentId;
    
    private Long userId;
    
    private Long enterpriseId;
    
    private Long resumeId;
    
    /**
     * 状态：1-已投递，2-已查看，3-面试中，4-已录用，5-已拒绝，6-已取消
     */
    private Integer status;
    
    private String coverLetter;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime applyTime;
    
    private LocalDateTime updateTime;
    
    private LocalDateTime interviewTime;
    
    private String interviewLocation;
    
    private String interviewContact;
    
    private String feedback;
}
