package com.recruitment.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.recruitment.auth.dto.LoginRequestDTO;
import com.recruitment.auth.dto.LoginResponseDTO;
import com.recruitment.auth.service.AuthService;
import com.recruitment.common.entity.User;
import com.recruitment.common.enums.ResultCode;
import com.recruitment.common.exception.BusinessException;
import com.recruitment.common.mapper.UserMapper;
import com.recruitment.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired(required = false)
    private RestTemplate restTemplate;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private final Map<String, String> verificationCodeCache = new ConcurrentHashMap<>();
    private final Map<String, Long> verificationCodeTimeCache = new ConcurrentHashMap<>();
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    /**
     * 用户类型映射：1-学生，2-企业，3-学校/教师，4-管理员
     */
    private String getUserRole(Integer userType) {
        if (userType == null) return "student";
        switch (userType) {
            case 1: return "student";
            case 2: return "enterprise";
            case 3: return "teacher";
            case 4: return "admin";
            default: return "student";
        }
    }
    
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        String phone = loginRequest.getPhone();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String verificationCode = loginRequest.getVerificationCode();
        
        User user = null;
        
        // 优先根据手机号查询用户
        if (phone != null && !phone.isEmpty()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            user = userMapper.selectOne(queryWrapper);
        }
        
        // 如果手机号没有找到，根据用户名查询
        if (user == null && username != null && !username.isEmpty()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username);
            user = userMapper.selectOne(queryWrapper);
        }
        
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 用户状态检查
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_STATUS_ABNORMAL);
        }
        
        boolean isPasswordLogin = password != null && !password.isEmpty();
        boolean isSmsLogin = verificationCode != null && !verificationCode.isEmpty();
        
        // 密码登录验证
        if (isPasswordLogin) {
            boolean passwordMatch = false;
            try {
                passwordMatch = passwordEncoder.matches(password, user.getPassword());
            } catch (Exception e) {
                passwordMatch = false;
            }
            if (!passwordMatch && password.equals(user.getPassword())) {
                passwordMatch = true;
            }
            if (!passwordMatch) {
                throw new BusinessException(ResultCode.PASSWORD_OR_ACCOUNT_ERROR);
            }
        } 
        // 验证码登录验证
        else if (isSmsLogin) {
            String storedCode = verificationCodeCache.get(phone);
            Long sendTime = verificationCodeTimeCache.get(phone);
            
            if (storedCode == null || sendTime == null) {
                throw new BusinessException(ResultCode.VERIFICATION_CODE_INVALID);
            }
            
            if (System.currentTimeMillis() - sendTime > 300000) {
                verificationCodeCache.remove(phone);
                verificationCodeTimeCache.remove(phone);
                throw new BusinessException(ResultCode.VERIFICATION_CODE_EXPIRED);
            }
            
            if (!storedCode.equals(verificationCode)) {
                throw new BusinessException(ResultCode.VERIFICATION_CODE_ERROR);
            }
            
            // 验证成功后删除验证码
            verificationCodeCache.remove(phone);
            verificationCodeTimeCache.remove(phone);
        } 
        else {
            throw new BusinessException(ResultCode.PARAM_INVALID, "请提供密码或验证码");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());
        
        // 将Token存入内存缓存
        tokenCache.put("token:" + token, user.getId().toString());
        
        // 设置24小时后过期
        new Thread(() -> {
            try {
                Thread.sleep(24 * 60 * 60 * 1000L);
                tokenCache.remove("token:" + token);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        // 构建响应
        return LoginResponseDTO.builder()
                .token(token)
                .userInfo(LoginResponseDTO.UserInfoDTO.builder()
                        .id(user.getId())
                        .phone(user.getPhone())
                        .username(user.getUsername())
                        .realName(user.getRealName())
                        .role(getUserRole(user.getUserType()))
                        .avatar(user.getAvatar())
                        .build())
                .build();
    }
    
    @Override
    public void register(User user) {
        // 检查手机号是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, user.getPhone());
        if (userMapper.exists(queryWrapper)) {
            throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
        }
        
        // 检查用户名是否已存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.exists(queryWrapper)) {
            throw new BusinessException(ResultCode.USERNAME_ALREADY_EXISTS);
        }
        
        // 密码加密（如果为空则生成默认密码）
        String password = user.getPassword();
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        user.setPassword(passwordEncoder.encode(password));
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1); // 正常状态
        }
        
        // 保存用户
        userMapper.insert(user);
    }
    
    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        tokenCache.remove("token:" + token);
    }
    
    @Override
    public String refreshToken(String oldToken) {
        if (oldToken != null && oldToken.startsWith("Bearer ")) {
            oldToken = oldToken.substring(7);
        }
        
        // 验证旧Token
        if (!jwtUtil.validateToken(oldToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
        
        // 获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(oldToken);
        String username = jwtUtil.getUsernameFromToken(oldToken);
        Integer userType = jwtUtil.getUserTypeFromToken(oldToken);
        
        // 生成新Token
        String newToken = jwtUtil.generateToken(userId, username, userType);
        
        // 删除旧Token，存入新Token
        tokenCache.remove("token:" + oldToken);
        tokenCache.put("token:" + newToken, userId.toString());
        
        // 设置24小时后过期
        new Thread(() -> {
            try {
                Thread.sleep(24 * 60 * 60 * 1000L);
                tokenCache.remove("token:" + newToken);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        return newToken;
    }
    
    @Override
    public boolean validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        token = token.substring(7);
        
        // 检查内存缓存中是否存在
        String storedUserId = tokenCache.get("token:" + token);
        if (storedUserId == null) {
            return false;
        }
        
        // 验证JWT
        return jwtUtil.validateToken(token);
    }
    
    @Override
    public void sendVerificationCode(String phone) {
        long currentTime = System.currentTimeMillis();
        Long lastSendTime = verificationCodeTimeCache.get(phone);
        
        if (lastSendTime != null && currentTime - lastSendTime < 60000) {
            throw new BusinessException(ResultCode.VERIFICATION_CODE_SEND_TOO_FAST);
        }
        
        String code = String.format("%06d", random.nextInt(999999));
        System.out.println("===== 虚拟验证码生成 =====");
        System.out.println("手机号: " + phone);
        System.out.println("验证码: " + code);
        System.out.println("有效期: 5分钟");
        System.out.println("==========================");
        
        verificationCodeCache.put(phone, code);
        verificationCodeTimeCache.put(phone, currentTime);
        
        try {
            if (restTemplate != null) {
                String smsUrl = "http://localhost:8089/sms/send";
                restTemplate.postForObject(smsUrl, Map.of("phone", phone, "code", code), String.class);
            }
        } catch (Exception e) {
            System.out.println("短信服务调用失败，使用内存存储验证码: " + e.getMessage());
        }
    }
    
    @Override
    public boolean verifyCode(String phone, String code) {
        String storedCode = verificationCodeCache.get(phone);
        Long sendTime = verificationCodeTimeCache.get(phone);
        
        if (storedCode == null || sendTime == null) {
            return false;
        }
        
        if (System.currentTimeMillis() - sendTime > 300000) {
            verificationCodeCache.remove(phone);
            verificationCodeTimeCache.remove(phone);
            return false;
        }
        
        boolean isValid = storedCode.equals(code);
        if (isValid) {
            verificationCodeCache.remove(phone);
            verificationCodeTimeCache.remove(phone);
        }
        
        return isValid;
    }
}
