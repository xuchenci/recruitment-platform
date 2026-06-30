package com.recruitment.auth.service;

import com.recruitment.auth.dto.LoginRequestDTO;
import com.recruitment.auth.dto.LoginResponseDTO;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponseDTO login(LoginRequestDTO loginRequest);
    
    /**
     * 用户注册
     * @param user 用户信息
     */
    void register(com.recruitment.common.entity.User user);
    
    /**
     * 登出
     * @param token JWT令牌
     */
    void logout(String token);
    
    /**
     * 刷新Token
     * @param oldToken 旧Token
     * @return 新Token
     */
    String refreshToken(String oldToken);
    
    /**
     * 验证Token
     * @param token JWT令牌
     * @return 是否有效
     */
    boolean validateToken(String token);
    
    /**
     * 发送验证码
     * @param phone 手机号
     */
    void sendVerificationCode(String phone);
    
    /**
     * 验证验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 是否有效
     */
    boolean verifyCode(String phone, String code);
}
