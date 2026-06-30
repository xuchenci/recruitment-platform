package com.recruitment.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(
    scanBasePackages = "com.recruitment",
    exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class}
)
@MapperScan("com.recruitment.common.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("Auth Service started on port 8081");
    }
    
    /**
     * 配置RestTemplate用于调用其他服务
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
