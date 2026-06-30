package com.recruitment.job.controller;

import com.recruitment.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职位收藏控制器
 */
@RestController
@RequestMapping("/jobs/favorite")
public class FavoriteController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 收藏职位
     */
    @PostMapping("/add")
    public Result<?> addFavorite(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long jobId = request.get("jobId");

        if (studentId == null || jobId == null) {
            return Result.error(400, "参数不能为空");
        }

        try {
            String sql = "INSERT INTO job_favorite (student_id, job_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, studentId, jobId);
            return Result.success("收藏成功");
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return Result.error(400, "已收藏该职位");
            }
            return Result.error(500, "收藏失败: " + e.getMessage());
        }
    }

    /**
     * 取消收藏
     */
    @PostMapping("/remove")
    public Result<?> removeFavorite(@RequestBody Map<String, Long> request) {
        Long studentId = request.get("studentId");
        Long jobId = request.get("jobId");

        if (studentId == null || jobId == null) {
            return Result.error(400, "参数不能为空");
        }

        String sql = "DELETE FROM job_favorite WHERE student_id = ? AND job_id = ?";
        int rows = jdbcTemplate.update(sql, studentId, jobId);
        
        if (rows > 0) {
            return Result.success("取消收藏成功");
        } else {
            return Result.error(400, "未收藏该职位");
        }
    }

    /**
     * 获取用户收藏的职位列表
     */
    @GetMapping("/list")
    public Result<?> getFavoriteList(@RequestParam Long studentId) {
        if (studentId == null) {
            return Result.error(400, "学生ID不能为空");
        }

        String sql = "SELECT jf.job_id, j.job_title, j.salary_min, j.salary_max, j.city, j.experience_requirement, " +
                     "j.education_requirement, j.view_count, e.company_name, e.industry, e.company_size " +
                     "FROM job_favorite jf " +
                     "JOIN job j ON jf.job_id = j.id " +
                     "JOIN enterprise e ON j.enterprise_id = e.id " +
                     "WHERE jf.student_id = ? AND j.status = 1 " +
                     "ORDER BY jf.created_at DESC";

        List<Map<String, Object>> favorites = jdbcTemplate.queryForList(sql, studentId);

        // 处理返回数据
        for (Map<String, Object> fav : favorites) {
            fav.put("salaryRange", fav.get("salary_min") + "K-" + fav.get("salary_max") + "K");
            fav.remove("salary_min");
            fav.remove("salary_max");
        }

        return Result.success(favorites);
    }

    /**
     * 检查职位是否已收藏
     */
    @GetMapping("/check")
    public Result<?> checkFavorite(@RequestParam Long studentId, @RequestParam Long jobId) {
        if (studentId == null || jobId == null) {
            return Result.error(400, "参数不能为空");
        }

        String sql = "SELECT COUNT(*) FROM job_favorite WHERE student_id = ? AND job_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, studentId, jobId);

        Map<String, Object> result = new HashMap<>();
        result.put("isFavorite", count != null && count > 0);
        return Result.success(result);
    }
}