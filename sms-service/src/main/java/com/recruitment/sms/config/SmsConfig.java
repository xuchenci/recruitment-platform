package com.recruitment.sms.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信配置
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsConfig {
    
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String regionId;
    private String signName;
    private TemplateCode templateCode;
    
    public static class TemplateCode {
        private String verificationCode;
        private String notification;
        private String interviewNotification;
        
        // Getters and Setters
        public String getVerificationCode() {
            return verificationCode;
        }
        
        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
        
        public String getNotification() {
            return notification;
        }
        
        public void setNotification(String notification) {
            this.notification = notification;
        }
        
        public String getInterviewNotification() {
            return interviewNotification;
        }
        
        public void setInterviewNotification(String interviewNotification) {
            this.interviewNotification = interviewNotification;
        }
    }
    
    /**
     * 创建阿里云短信客户端
     */
    @Bean
    public Client smsClient() throws Exception {
        Config config = new Config()
            .setAccessKeyId(accessKeyId)
            .setAccessKeySecret(accessKeySecret)
            .setEndpoint(endpoint);
        
        return new Client(config);
    }
    
    // Getters and Setters
    public String getAccessKeyId() {
        return accessKeyId;
    }
    
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public String getRegionId() {
        return regionId;
    }
    
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    
    public String getSignName() {
        return signName;
    }
    
    public void setSignName(String signName) {
        this.signName = signName;
    }
    
    public TemplateCode getTemplateCode() {
        return templateCode;
    }
    
    public void setTemplateCode(TemplateCode templateCode) {
        this.templateCode = templateCode;
    }
}
