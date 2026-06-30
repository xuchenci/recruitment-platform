package com.recruitment.sms.consumer;

import com.recruitment.sms.service.SmsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RocketMQ 短信消费者
 * 监听 sms-topic 主题，异步发送短信
 */
@Component
@RocketMQMessageListener(
    topic = "sms-topic",
    selectorExpression = "*",
    consumerGroup = "sms-consumer-group",
    selectorType = SelectorType.TAG
)
public class SmsConsumer implements RocketMQListener<String> {
    
    @Autowired
    private SmsService smsService;
    
    @Override
    public void onMessage(String message) {
        System.out.println("📨 收到短信发送请求：" + message);
        
        try {
            // 消息格式：
            // 1. 验证码：phone|code
            // 2. 通知：phone|notification|message
            // 3. 面试通知：phone|interview|candidateName|interviewTime|location
            
            String[] parts = message.split("\\|");
            
            if (parts.length < 2) {
                System.err.println("⚠️ 消息格式错误：" + message);
                return;
            }
            
            String phone = parts[0];
            String type = parts.length > 2 ? parts[1] : "verification";
            
            switch (type) {
                case "verification":
                    // 格式：phone|code
                    String code = parts[1];
                    boolean success = smsService.sendVerificationCode(phone, code);
                    if (success) {
                        System.out.println("✅ 验证码短信已发送：" + phone);
                    } else {
                        System.err.println("❌ 验证码短信发送失败：" + phone);
                        // TODO: 发送到重试队列或者死信队列
                    }
                    break;
                    
                case "notification":
                    // 格式：phone|notification|message
                    String notification = parts[2];
                    success = smsService.sendNotification(phone, notification);
                    if (success) {
                        System.out.println("✅ 通知短信已发送：" + phone);
                    } else {
                        System.err.println("❌ 通知短信发送失败：" + phone);
                    }
                    break;
                    
                case "interview":
                    // 格式：phone|interview|candidateName|interviewTime|location
                    if (parts.length >= 5) {
                        String candidateName = parts[2];
                        String interviewTime = parts[3];
                        String location = parts[4];
                        success = smsService.sendInterviewNotification(phone, candidateName, interviewTime, location);
                        if (success) {
                            System.out.println("✅ 面试通知短信已发送：" + phone);
                        } else {
                            System.err.println("❌ 面试通知短信发送失败：" + phone);
                        }
                    }
                    break;
                    
                default:
                    System.err.println("⚠️ 未知的短信类型：" + type);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 处理短信消息异常：" + e.getMessage());
            e.printStackTrace();
            // TODO: 发送到重试队列
        }
    }
}
