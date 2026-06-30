package com.recruitment.notification.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RocketMQ消费者 - 发送短信
 */
@Service
@RocketMQMessageListener(
    topic = "sms-topic",
    consumerGroup = "notification-consumer-group",
    selectorExpression = "*"
)
public class SmsConsumerService implements RocketMQListener<String> {
    
    @Autowired
    private SmsService smsService;
    
    @Override
    public void onMessage(String message) {
        // 消息格式：phone|code 或 phone|notification|message
        String[] parts = message.split("\\|");
        
        if (parts.length == 2) {
            // 发送验证码
            String phone = parts[0];
            String code = parts[1];
            boolean success = smsService.sendVerificationCode(phone, code);
            System.out.println("发送验证码 " + phone + ": " + (success ? "成功" : "失败"));
            
        } else if (parts.length == 3 && "notification".equals(parts[1])) {
            // 发送通知
            String phone = parts[0];
            String notification = parts[2];
            boolean success = smsService.sendNotification(phone, notification);
            System.out.println("发送通知 " + phone + ": " + (success ? "成功" : "失败"));
        }
    }
}
