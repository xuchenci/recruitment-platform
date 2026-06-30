package com.recruitment.notification.controller;

import com.recruitment.notification.service.SmsService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    
    @Autowired
    private SmsService smsService;
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    /**
     * 发送验证码（通过RocketMQ异步发送）
     */
    @PostMapping("/sms/verification-code")
    public Map<String, Object> sendVerificationCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        
        // 生成验证码
        String code = smsService.generateCode(6);
        
        // 发送到RocketMQ队列异步处理
        String message = phone + "|" + code;
        rocketMQTemplate.convertAndSend("sms-topic", message);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "验证码已发送到 " + phone);
        result.put("code", code); // 实际生产环境不应返回code
        
        return result;
    }
    
    /**
     * 发送通知短信
     */
    @PostMapping("/sms/notify")
    public Map<String, Object> sendNotification(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String notification = request.get("message");
        
        // 发送到RocketMQ队列异步处理
        String message = phone + "|notification|" + notification;
        rocketMQTemplate.convertAndSend("sms-topic", message);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "通知已加入发送队列");
        
        return result;
    }
    
    /**
     * 面试通知
     */
    @PostMapping("/interview")
    public Map<String, Object> sendInterviewNotification(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String candidateName = request.get("candidateName");
        String interviewTime = request.get("interviewTime");
        String location = request.get("location");
        
        String notification = String.format(
            "您好 %s，您的面试已安排：时间 %s，地点 %s。请准时参加！",
            candidateName, interviewTime, location
        );
        
        String message = phone + "|notification|" + notification;
        rocketMQTemplate.convertAndSend("sms-topic", message);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "面试通知已发送");
        
        return result;
    }
    
    /**
     * 申请状态更新通知
     */
    @PostMapping("/application-status")
    public Map<String, Object> sendApplicationStatusNotification(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String candidateName = request.get("candidateName");
        String jobTitle = request.get("jobTitle");
        String status = request.get("status");
        
        String statusText = "";
        switch (status) {
            case "2": statusText = "已查看"; break;
            case "3": statusText = "面试中"; break;
            case "4": statusText = "已录用"; break;
            case "5": statusText = "未通过"; break;
            default: statusText = "状态更新";
        }
        
        String notification = String.format(
            "您好 %s，您申请的职位【%s】状态已更新：%s",
            candidateName, jobTitle, statusText
        );
        
        String message = phone + "|notification|" + notification;
        rocketMQTemplate.convertAndSend("sms-topic", message);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "状态更新通知已发送");
        
        return result;
    }
}
