package com.recruitment.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * AI服务启动类
 * 集成阿里百炼大模型，提供简历优化、人岗匹配等AI能力
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.recruitment.ai", "com.recruitment.common"})
@MapperScan("com.recruitment.common.mapper")
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
}