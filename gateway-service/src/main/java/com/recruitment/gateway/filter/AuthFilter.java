package com.recruitment.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证过滤器
 * 验证Token是否有效
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求路径
        String path = exchange.getRequest().getPath().value();
        
        // 不需要认证的路径
        if (path.contains("/api/auth/login") 
                || path.contains("/api/auth/register")
                || path.contains("/api/auth/sms-code")
                || path.contains("/api/auth/qrcode")
                || path.contains("/api/jobs/search")
                || path.contains("/api/jobs/")
                || path.contains("/api/students/list")
                || path.contains("/api/students/health")
                || path.contains("/api/analytics/dashboard")
                || path.contains("/api/enterprise/list")
                || path.contains("/api/ai/")
                || path.contains("/api/resume/preview/")
                || path.contains("/api/application/my")) {
            return chain.filter(exchange);
        }
        
        // 获取Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        // TODO: 验证Token是否有效（调用auth-service或直接验证JWT）
        // 这里简化为直接放行
        return chain.filter(exchange);
    }
    
    @Override
    public int getOrder() {
        return -100; // 优先级最高
    }
}
