package com.recruitment.analytics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.recruitment.analytics.service.AdminService;
import com.recruitment.common.entity.Enterprise;
import com.recruitment.common.entity.Student;
import com.recruitment.common.entity.User;
import com.recruitment.common.enums.ResultCode;
import com.recruitment.common.exception.BusinessException;
import com.recruitment.common.mapper.EnterpriseMapper;
import com.recruitment.common.mapper.StudentMapper;
import com.recruitment.common.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final Random random = new Random();

    private Integer mapRoleToUserType(String role) {
        if (role == null) return 1;
        switch (role) {
            case "student": return 1;
            case "enterprise": return 2;
            case "teacher": return 3;
            case "admin": return 4;
            default: return 1;
        }
    }

    private Integer mapStatus(String status) {
        if ("disabled".equals(status)) return 0;
        return 1;
    }

    @Override
    public IPage<User> getUserList(int page, int size, String keyword, String role, String status) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getPhone, keyword)
                    .or().like(User::getRealName, keyword));
        }

        if (role != null && !role.isEmpty()) {
            wrapper.eq(User::getUserType, mapRoleToUserType(role));
        }

        if (status != null && !status.isEmpty()) {
            wrapper.eq(User::getStatus, mapStatus(status));
        }

        wrapper.orderByDesc(User::getCreatedAt);
        return userMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional
    public User createUser(Map<String, Object> params) {
        String username = (String) params.get("username");
        String phone = (String) params.get("phone");
        String realName = (String) params.get("realName");
        String role = (String) params.get("role");
        String password = (String) params.get("password");

        if (username == null || username.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_INVALID, "用户名不能为空");
        }
        if (phone == null || phone.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_INVALID, "手机号不能为空");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (userMapper.exists(wrapper)) {
            throw new BusinessException(ResultCode.USERNAME_ALREADY_EXISTS);
        }

        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        if (userMapper.exists(wrapper)) {
            throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setRealName(realName);
        user.setUserType(mapRoleToUserType(role));
        user.setStatus(1);

        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        user.setPassword(passwordEncoder.encode(password));

        userMapper.insert(user);

        if ("student".equals(role)) {
            Student student = new Student();
            student.setUserId(user.getId());
            student.setCollege((String) params.get("school"));
            student.setMajor((String) params.get("major"));
            student.setRealName(realName);
            studentMapper.insert(student);
        } else if ("enterprise".equals(role)) {
            Enterprise enterprise = new Enterprise();
            enterprise.setUserId(user.getId());
            enterprise.setCompanyName((String) params.get("enterpriseName"));
            enterpriseMapper.insert(enterprise);
        }

        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, Map<String, Object> params) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (params.containsKey("username")) {
            String username = (String) params.get("username");
            if (!username.equals(user.getUsername())) {
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(User::getUsername, username);
                if (userMapper.exists(wrapper)) {
                    throw new BusinessException(ResultCode.USERNAME_ALREADY_EXISTS);
                }
                user.setUsername(username);
            }
        }

        if (params.containsKey("phone")) {
            String phone = (String) params.get("phone");
            if (!phone.equals(user.getPhone())) {
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(User::getPhone, phone);
                if (userMapper.exists(wrapper)) {
                    throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
                }
                user.setPhone(phone);
            }
        }

        if (params.containsKey("realName")) {
            user.setRealName((String) params.get("realName"));
        }

        if (params.containsKey("role")) {
            user.setUserType(mapRoleToUserType((String) params.get("role")));
        }

        if (params.containsKey("status")) {
            user.setStatus(mapStatus((String) params.get("status")));
        }

        if (params.containsKey("email")) {
            user.setEmail((String) params.get("email"));
        }

        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (user.getUserType() != null && user.getUserType() == 4) {
            throw new BusinessException(ResultCode.FORBIDDEN, "不能删除管理员账号");
        }

        LambdaQueryWrapper<Student> studentWrapper = new LambdaQueryWrapper<>();
        studentWrapper.eq(Student::getUserId, id);
        studentMapper.delete(studentWrapper);

        LambdaQueryWrapper<Enterprise> enterpriseWrapper = new LambdaQueryWrapper<>();
        enterpriseWrapper.eq(Enterprise::getUserId, id);
        enterpriseMapper.delete(enterpriseWrapper);

        userMapper.deleteById(id);
    }

    @Override
    public void resetPassword(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String newPassword = String.format("%06d", random.nextInt(999999));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        System.out.println("===== 密码重置 =====");
        System.out.println("用户ID: " + id);
        System.out.println("手机号: " + user.getPhone());
        System.out.println("新密码: " + newPassword);
        System.out.println("==================");
    }
}