package com.recruitment.analytics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.recruitment.common.entity.User;

import java.util.Map;

public interface AdminService {

    IPage<User> getUserList(int page, int size, String keyword, String role, String status);

    User getUserById(Long id);

    User createUser(Map<String, Object> params);

    User updateUser(Long id, Map<String, Object> params);

    void deleteUser(Long id);

    void resetPassword(Long id);
}