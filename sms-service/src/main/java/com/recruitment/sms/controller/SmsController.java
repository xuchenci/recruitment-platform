package com.recruitment.sms.controller;

import com.recruitment.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送控制器
 * 提供REST API接口，可用于手动触发短信发送（通常用于测试）
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    
    @Autowired
    private SmsService smsService;
    
    /**
     * 发送验证码短信（测试接口）
     */
    @PostMapping("/verification-code")
    public Map<String, Object> sendVerificationCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String code = request.get("code");
        
        boolean success = smsService.sendVerificationCode(phone, code);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "验证码发送成功" : "验证码发送失败");
        return result;
    }
    
    /**
     * 发送通知短信（测试接口）
     */
    @PostMapping("/notification")
    public Map<String, Object> sendNotification(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String message = request.get("message");
        
        boolean success = smsService.sendNotification(phone, message);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "通知发送成功" : "通知发送失败");
        return result;
    }
    
    /**
     * 发送面试通知短信（测试接口）
     */
    @PostMapping("/interview")
    public Map<String, Object> sendInterviewNotification(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String candidateName = request.get("candidateName");
        String interviewTime = request.get("interviewTime");
        String location = request.get("location");
        
        boolean success = smsService.sendInterviewNotification(phone, candidateName, interviewTime, location);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "面试通知发送成功" : "面试通知发送失败");
        return result;
    }
    
    /**
     * 通用短信发送接口（测试接口）
     */
    @PostMapping("/send")
    public Map<String, Object> sendSms(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String templateCode = request.get("templateCode");
        String templateParam = request.get("templateParam");
        
        boolean success = smsService.sendSms(phone, templateCode, templateParam);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "短信发送成功" : "短信发送失败");
        return result;
    }
    
    /**
     * 生成随机验证码
     */
    @GetMapping("/generate-code")
    public Map<String, Object> generateCode(@RequestParam(defaultValue = "6") int length) {
        String code = "";
        for (int i = 0; i < length; i++) {
            code += (int) (Math.random() * 10);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("code", code);
        return result;
    }
}
