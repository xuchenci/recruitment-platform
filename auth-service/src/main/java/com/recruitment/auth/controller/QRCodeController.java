package com.recruitment.auth.controller;

import com.recruitment.auth.service.AuthService;
import com.recruitment.common.result.Result;
import com.recruitment.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 二维码登录控制器（使用内存存储）
 *
 * 扫码流程：
 * 1. PC端调用 /qrcode/generate 生成二维码token
 * 2. 手机端扫码打开 /scan-login?token=xxx H5页面
 * 3. H5页面输入账号密码，调用 /qrcode/scan 标记已扫码
 * 4. H5页面自动调用 /qrcode/confirm 确认登录
 * 5. PC端轮询 /qrcode/status 检测到 confirmed，获取jwtToken完成登录
 */
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    // 二维码有效期（秒）
    private static final long QR_CODE_EXPIRE_SECONDS = 300;

    // 二维码状态
    private static final String STATUS_WAITING = "waiting";
    private static final String STATUS_SCANNED = "scanned";
    private static final String STATUS_CONFIRMED = "confirmed";
    private static final String STATUS_EXPIRED = "expired";

    // 内存存储：token -> 状态
    private static final ConcurrentHashMap<String, String> qrCodeStatusMap = new ConcurrentHashMap<>();
    // 内存存储：token -> 用户信息（userId:username:phone:role）
    private static final ConcurrentHashMap<String, String> qrCodeUserInfoMap = new ConcurrentHashMap<>();
    // 内存存储：token -> 创建时间戳
    private static final ConcurrentHashMap<String, Long> qrCodeCreateTimeMap = new ConcurrentHashMap<>();

    // 定时清理过期的二维码
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public QRCodeController() {
        // 每60秒清理一次过期的二维码
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            qrCodeCreateTimeMap.keySet().removeIf(token -> {
                Long createTime = qrCodeCreateTimeMap.get(token);
                if (createTime == null || (now - createTime) > QR_CODE_EXPIRE_SECONDS * 1000) {
                    qrCodeStatusMap.remove(token);
                    qrCodeUserInfoMap.remove(token);
                    qrCodeCreateTimeMap.remove(token);
                    return true;
                }
                return false;
            });
        }, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 生成二维码登录Token
     */
    @GetMapping("/generate")
    public Result<Map<String, Object>> generateQRCode() {
        // 生成唯一token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 存储到内存，状态为waiting
        qrCodeStatusMap.put(token, STATUS_WAITING);
        qrCodeCreateTimeMap.put(token, System.currentTimeMillis());

        // 构建扫码URL，指向前端H5页面
        String scanUrl = frontendUrl + "/scan-login?token=" + token;

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("expireSeconds", QR_CODE_EXPIRE_SECONDS);
        result.put("status", STATUS_WAITING);
        result.put("scanUrl", scanUrl);

        // 设置过期删除
        scheduler.schedule(() -> {
            qrCodeStatusMap.remove(token);
            qrCodeUserInfoMap.remove(token);
            qrCodeCreateTimeMap.remove(token);
        }, QR_CODE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        return Result.success(result);
    }

    /**
     * 查询二维码扫码状态
     * 前端轮询此接口检查扫码状态
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> checkStatus(@RequestParam String token) {
        String status = qrCodeStatusMap.get(token);

        if (status == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", STATUS_EXPIRED);
            result.put("message", "二维码已过期，请刷新");
            return Result.success(result);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", status);

        // 如果状态为confirmed，返回登录信息
        if (STATUS_CONFIRMED.equals(status)) {
            String userInfoStr = qrCodeUserInfoMap.get(token);

            if (userInfoStr != null) {
                String[] parts = userInfoStr.split(":");
                if (parts.length >= 4) {
                    Long userId = Long.parseLong(parts[0]);
                    String username = parts[1];
                    String phone = parts[2];
                    String role = parts[3];

                    // 用户类型映射：student=1, enterprise=2, teacher=3, admin=4
                    Integer userType = mapRoleToUserType(role);

                    // 生成JWT token（第二个参数传username，与正常登录一致）
                    String jwtToken = jwtUtil.generateToken(userId, username, userType);

                    result.put("jwtToken", jwtToken);
                    result.put("userId", userId);
                    result.put("phone", phone);
                    result.put("role", role);

                    qrCodeStatusMap.remove(token);
                    qrCodeUserInfoMap.remove(token);
                    qrCodeCreateTimeMap.remove(token);
                }
            }
            result.put("message", "登录成功");
        } else if (STATUS_SCANNED.equals(status)) {
            result.put("message", "已扫码，请在手机上确认登录");
        } else if (STATUS_WAITING.equals(status)) {
            result.put("message", "等待扫码");
        }

        return Result.success(result);
    }

    /**
     * 将角色字符串映射为用户类型数字
     * 1-学生, 2-企业, 3-学校/教师, 4-管理员
     */
    private Integer mapRoleToUserType(String role) {
        switch (role.toLowerCase()) {
            case "student": return 1;
            case "enterprise": return 2;
            case "teacher": return 3;
            case "admin": return 4;
            default: return 1;
        }
    }

    /**
     * 手机端扫码接口
     */
    @PostMapping("/scan")
    public Result<String> scanQRCode(
            @RequestParam String token,
            @RequestParam Long userId,
            @RequestParam String phone,
            @RequestParam String role,
            @RequestParam(required = false, defaultValue = "") String username) {

        String status = qrCodeStatusMap.get(token);

        if (status == null) {
            return Result.error("二维码已过期");
        }

        if (!STATUS_WAITING.equals(status)) {
            return Result.error("二维码状态异常");
        }

        qrCodeStatusMap.put(token, STATUS_SCANNED);

        // 存储 userId:username:phone:role
        String userInfo = userId + ":" + (username != null ? username : phone) + ":" + phone + ":" + role;
        qrCodeUserInfoMap.put(token, userInfo);

        return Result.success("扫码成功，请在手机上确认登录");
    }

    /**
     * 手机端确认登录
     */
    @PostMapping("/confirm")
    public Result<String> confirmLogin(@RequestParam String token) {
        String status = qrCodeStatusMap.get(token);

        if (status == null) {
            return Result.error("二维码已过期");
        }

        if (!STATUS_SCANNED.equals(status)) {
            return Result.error("请先扫码");
        }

        qrCodeStatusMap.put(token, STATUS_CONFIRMED);

        return Result.success("登录确认成功");
    }

    /**
     * 取消扫码登录
     */
    @PostMapping("/cancel")
    public Result<String> cancelLogin(@RequestParam String token) {
        qrCodeStatusMap.remove(token);
        qrCodeUserInfoMap.remove(token);
        qrCodeCreateTimeMap.remove(token);

        return Result.success("已取消登录");
    }
}