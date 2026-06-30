package com.recruitment.notification.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Random;

/**
 * 短信服务
 */
@Service
public class SmsService {
    
    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;
    
    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;
    
    @Value("${aliyun.sms.sign-name}")
    private String signName;
    
    @Value("${aliyun.sms.template-code}")
    private String templateCode;
    
    /**
     * 发送短信验证码
     */
    public boolean sendVerificationCode(String phone, String code) {
        try {
            // 创建阿里云短信客户端
            Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
            
            Client client = new Client(config);
            
            // 创建请求
            SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam("{\"code\":\"" + code + "\"}");
            
            // 发送短信
            SendSmsResponse response = client.sendSms(request);
            
            return response.getBody().getCode().equals("OK");
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 发送通知短信
     */
    public boolean sendNotification(String phone, String message) {
        try {
            Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
            
            Client client = new Client(config);
            
            SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode("SMS_NOTIFICATION") // 通知模板Code
                .setTemplateParam("{\"message\":\"" + message + "\"}");
            
            SendSmsResponse response = client.sendSms(request);
            
            return response.getBody().getCode().equals("OK");
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 生成随机验证码
     */
    public String generateCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
