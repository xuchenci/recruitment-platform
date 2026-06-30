package com.recruitment.auth.controller;

import com.recruitment.auth.dto.LoginRequestDTO;
import com.recruitment.auth.dto.LoginResponseDTO;
import com.recruitment.auth.service.AuthService;
import com.recruitment.common.entity.User;
import com.recruitment.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 登录
     * 支持密码登录和验证码登录
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 注册
     * @param params 注册参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody Map<String, Object> params) {
        try {
            User user = new User();
            user.setUsername((String) params.get("username"));
            user.setPhone((String) params.get("phone"));
            user.setPassword((String) params.get("password"));
            
            // 角色映射：student->1, enterprise->2, teacher->3
            String role = (String) params.get("role");
            if (role != null) {
                switch (role) {
                    case "student": user.setUserType(1); break;
                    case "enterprise": user.setUserType(2); break;
                    case "teacher": user.setUserType(3); break;
                    default: user.setUserType(1); break;
                }
            }
            
            authService.register(user);
            return Result.success("注册成功");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 登出
     * @param token JWT令牌
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return Result.success("登出成功");
    }
    
    /**
     * 刷新Token
     * @param oldToken 旧Token
     * @return 新Token
     */
    @PostMapping("/refresh-token")
    public Result<String> refreshToken(@RequestHeader("Authorization") String oldToken) {
        try {
            String newToken = authService.refreshToken(oldToken);
            return Result.success(newToken);
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }
    
    /**
     * 发送验证码
     * @param phone 手机号
     * @return 发送结果
     */
    @PostMapping("/verification-code")
    public Result<String> sendVerificationCode(@RequestParam String phone) {
        // 验证手机号格式
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return Result.error("请输入正确的手机号");
        }
        
        try {
            authService.sendVerificationCode(phone);
            return Result.success("验证码已发送");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
