package com.recruitment.application.controller;

import com.recruitment.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/unread-count")
    public Result<?> getUnreadCount(@RequestParam Long userId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
            "SELECT id FROM notification WHERE receiver_id = ? AND is_read = 0", userId);
        return Result.success(rows.size());
    }

    @GetMapping("/messages")
    public Result<?> getMessages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam Long userId) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
            "SELECT id, title, content, type, is_read, related_id, created_at " +
            "FROM notification WHERE receiver_id = ? ORDER BY created_at DESC LIMIT ?, ?",
            userId, offset, size);
        return Result.success(list);
    }

    @PutMapping("/messages/{id}/read")
    public Result<?> markAsRead(@PathVariable Long id) {
        jdbcTemplate.update("UPDATE notification SET is_read = 1 WHERE id = ?", id);
        return Result.success("已读");
    }

    @PutMapping("/read-all")
    public Result<?> markAllRead(@RequestParam Long userId) {
        jdbcTemplate.update("UPDATE notification SET is_read = 1 WHERE receiver_id = ?", userId);
        return Result.success("全部已读");
    }

    public void createNotification(Long userId, String title, String content, String type, Long refId) {
        try {
            int typeInt = 1;
            try { typeInt = Integer.parseInt(type); } catch (NumberFormatException ignored) {}
            jdbcTemplate.update(
                "INSERT INTO notification (receiver_id, title, content, type, related_id) VALUES (?,?,?,?,?)",
                userId, title, content, typeInt, refId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
