package com.recruitment.auth.dto;

/**
 * 登录响应DTO
 */
public class LoginResponseDTO {
    
    private String token;
    private UserInfoDTO userInfo;
    
    public LoginResponseDTO() {
    }
    
    public LoginResponseDTO(String token, UserInfoDTO userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserInfoDTO getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String token;
        private UserInfoDTO userInfo;
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public Builder userInfo(UserInfoDTO userInfo) {
            this.userInfo = userInfo;
            return this;
        }
        
        public LoginResponseDTO build() {
            return new LoginResponseDTO(token, userInfo);
        }
    }
    
    /**
     * 用户信息内部类
     */
    public static class UserInfoDTO {
        
        private Long id;
        private String phone;
        private String username;
        private String realName;
        private String role;
        private String avatar;
        private String enterpriseName;
        
        public UserInfoDTO() {
        }
        
        public UserInfoDTO(Long id, String phone, String username, String realName, String role, String avatar, String enterpriseName) {
            this.id = id;
            this.phone = phone;
            this.username = username;
            this.realName = realName;
            this.role = role;
            this.avatar = avatar;
            this.enterpriseName = enterpriseName;
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
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
        
        public String getRealName() {
            return realName;
        }
        
        public void setRealName(String realName) {
            this.realName = realName;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getAvatar() {
            return avatar;
        }
        
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        
        public String getEnterpriseName() {
            return enterpriseName;
        }
        
        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }
        
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private Long id;
            private String phone;
            private String username;
            private String realName;
            private String role;
            private String avatar;
            private String enterpriseName;
            
            public Builder id(Long id) {
                this.id = id;
                return this;
            }
            
            public Builder phone(String phone) {
                this.phone = phone;
                return this;
            }
            
            public Builder username(String username) {
                this.username = username;
                return this;
            }
            
            public Builder realName(String realName) {
                this.realName = realName;
                return this;
            }
            
            public Builder role(String role) {
                this.role = role;
                return this;
            }
            
            public Builder avatar(String avatar) {
                this.avatar = avatar;
                return this;
            }
            
            public Builder enterpriseName(String enterpriseName) {
                this.enterpriseName = enterpriseName;
                return this;
            }
            
            public UserInfoDTO build() {
                return new UserInfoDTO(id, phone, username, realName, role, avatar, enterpriseName);
            }
        }
    }
}