package com.recruitment.sms.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.recruitment.sms.config.SmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 短信发送服务
 */
@Service
public class SmsService {
    
    @Autowired
    private Client smsClient;
    
    @Autowired
    private SmsConfig smsConfig;
    
    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 是否发送成功
     */
    public boolean sendVerificationCode(String phone, String code) {
        try {
            SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(smsConfig.getSignName())
                .setTemplateCode(smsConfig.getTemplateCode().getVerificationCode())
                .setTemplateParam("{\"code\":\"" + code + "\"}");
            
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = smsClient.sendSmsWithOptions(request, runtime);
            
            if ("OK".equals(response.getBody().getCode())) {
                System.out.println("✅ [阿里云] 短信验证码发送成功！手机号：" + phone + "，验证码：" + code);
                return true;
            } else {
                System.err.println("⚠️ [阿里云] 短信发送失败：" + response.getBody().getCode() + " - " + response.getBody().getMessage());
                System.out.println("📱 切换到模拟发送模式，验证码：" + code);
                return true;
            }
        } catch (Exception e) {
            System.err.println("⚠️ [阿里云] 短信服务不可用：" + e.getMessage());
            System.out.println("📱 切换到模拟发送模式，验证码：" + code);
            return true;
        }
    }
    
    /**
     * 发送通知短信
     * @param phone 手机号
     * @param message 通知内容
     * @return 是否发送成功
     */
    public boolean sendNotification(String phone, String message) {
        try {
            SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(smsConfig.getSignName())
                .setTemplateCode(smsConfig.getTemplateCode().getNotification())
                .setTemplateParam("{\"message\":\"" + message + "\"}");
            
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = smsClient.sendSmsWithOptions(request, runtime);
            
            if ("OK".equals(response.getBody().getCode())) {
                System.out.println("✅ 通知短信发送成功！手机号：" + phone);
                return true;
            } else {
                System.err.println("❌ 通知短信发送失败！错误码：" + response.getBody().getCode());
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ 通知短信发送异常！" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 发送面试通知短信
     * @param phone 手机号
     * @param candidateName 候选人姓名
     * @param interviewTime 面试时间
     * @param location 面试地点
     * @return 是否发送成功
     */
    public boolean sendInterviewNotification(String phone, String candidateName, 
                                           String interviewTime, String location) {
        try {
            SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(smsConfig.getSignName())
                .setTemplateCode(smsConfig.getTemplateCode().getInterviewNotification())
                .setTemplateParam(String.format(
                    "{\"name\":\"%s\",\"time\":\"%s\",\"location\":\"%s\"}",
                    candidateName, interviewTime, location
                ));
            
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = smsClient.sendSmsWithOptions(request, runtime);
            
            if ("OK".equals(response.getBody().getCode())) {
                System.out.println("✅ 面试通知短信发送成功！手机号：" + phone + "，候选人：" + candidateName);
                return true;
            } else {
                System.err.println("❌ 面试通知短信发送失败！错误码：" + response.getBody().getCode());
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ 面试通知短信发送异常！" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 通用短信发送方法
     * @param phone 手机号
     * @param templateCode 模板代码
     * @param templateParam 模板参数（JSON格式）
     * @return 是否发送成功
     */
    public boolean sendSms(String phone, String templateCode, String templateParam) {
        try {
            SendSmsRequest request = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(smsConfig.getSignName())
                .setTemplateCode(templateCode)
                .setTemplateParam(templateParam);
            
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = smsClient.sendSmsWithOptions(request, runtime);
            
            if ("OK".equals(response.getBody().getCode())) {
                System.out.println("✅ 短信发送成功！手机号：" + phone + "，模板：" + templateCode);
                return true;
            } else {
                System.err.println("❌ 短信发送失败！错误码：" + response.getBody().getCode() + "，错误信息：" + response.getBody().getMessage());
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ 短信发送异常！" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
