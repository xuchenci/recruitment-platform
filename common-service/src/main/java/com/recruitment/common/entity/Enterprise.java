package com.recruitment.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 企业实体类
 */
@Data
@TableName("enterprise")
public class Enterprise extends BaseEntity {
    
    private Long userId;
    
    private String companyName;
    
    private String companySize;
    
    private String industry;
    
    private String creditCode;
    
    private String financingStage;
    
    private String city;
    
    private String businessLicense;
    
    private String businessLicenseUrl;
    
    private String otherCertUrl;
    
    private String logo;
    
    private String description;
    
    private String address;
    
    private String website;
    
    private String contactName;
    
    private String contactPosition;
    
    private String contactPhone;
    
    private String contactEmail;
    
    private Integer verified;
    
    private LocalDateTime verifyTime;
    
    private Integer verifyStatus;
    
    private String rejectReason;
}
