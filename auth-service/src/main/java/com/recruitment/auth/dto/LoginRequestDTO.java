package com.recruitment.auth.dto;

/**
 * 登录请求DTO
 */
public class LoginRequestDTO {
    
    private String phone;
    private String username;
    private String password;
    private String verificationCode;
    
    public LoginRequestDTO() {
    }
    
    public LoginRequestDTO(String phone, String password, String verificationCode) {
        this.phone = phone;
        this.password = password;
        this.verificationCode = verificationCode;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}