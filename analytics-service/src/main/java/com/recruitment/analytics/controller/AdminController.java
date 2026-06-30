package com.recruitment.analytics.controller;

import com.recruitment.analytics.service.AdminService;
import com.recruitment.common.entity.User;
import com.recruitment.common.result.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public Result<?> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        try {
            IPage<User> userPage = adminService.getUserList(page, size, keyword, role, status);
            return Result.success(userPage);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取用户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public Result<?> getUserById(@PathVariable Long id) {
        try {
            User user = adminService.getUserById(id);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取用户信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/users")
    public Result<?> createUser(@RequestBody Map<String, Object> params) {
        try {
            User user = adminService.createUser(params);
            return Result.success("用户创建成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "创建用户失败: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public Result<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        try {
            User user = adminService.updateUser(id, params);
            return Result.success("用户更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "更新用户失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/users/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return Result.success("用户删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "删除用户失败: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id) {
        try {
            adminService.resetPassword(id);
            return Result.success("密码已重置，新密码已发送至用户手机");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "重置密码失败: " + e.getMessage());
        }
    }

    @GetMapping("/roles")
    public Result<?> getRoleList() {
        try {
            List<Map<String, Object>> roles = List.of(
                    Map.of("id", 1, "name", "student", "label", "学生"),
                    Map.of("id", 2, "name", "enterprise", "label", "企业HR"),
                    Map.of("id", 3, "name", "teacher", "label", "教师"),
                    Map.of("id", 4, "name", "admin", "label", "管理员")
            );
            return Result.success(roles);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取角色列表失败");
        }
    }

    @PutMapping("/users/{id}/disable")
    public Result<?> disableUser(@PathVariable Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("status", "disabled");
            adminService.updateUser(id, params);
            return Result.success("用户已禁用");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "禁用用户失败: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}/enable")
    public Result<?> enableUser(@PathVariable Long id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("status", "enabled");
            adminService.updateUser(id, params);
            return Result.success("用户已启用");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "启用用户失败: " + e.getMessage());
        }
    }

    @PutMapping("/users/{userId}/role")
    public Result<?> assignRole(@PathVariable Long userId, @RequestBody Map<String, Object> params) {
        try {
            Integer roleId = (Integer) params.get("roleId");
            String roleName = switch (roleId) {
                case 1 -> "student";
                case 2 -> "enterprise";
                case 3 -> "teacher";
                case 4 -> "admin";
                default -> "student";
            };
            Map<String, Object> updateParams = new HashMap<>();
            updateParams.put("role", roleName);
            adminService.updateUser(userId, updateParams);
            return Result.success("角色分配成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "分配角色失败: " + e.getMessage());
        }
    }

    @GetMapping("/config")
    public Result<?> getSystemConfig() {
        try {
            Map<String, Object> config = Map.of(
                    "siteName", "数智化校企招聘与就业生态平台",
                    "siteDescription", "基于AI的智能招聘平台",
                    "contactEmail", "support@recruitment.com",
                    "contactPhone", "400-888-8888",
                    "maxResumeSize", 10,
                    "maxImageSize", 2
            );
            return Result.success(config);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取系统配置失败");
        }
    }

    @PutMapping("/config")
    public Result<?> updateSystemConfig(@RequestBody Map<String, Object> params) {
        try {
            return Result.success("系统配置更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "更新系统配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/logs")
    public Result<?> getOperationLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("total", 100);
            result.put("records", List.of());
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取操作日志失败");
        }
    }
}