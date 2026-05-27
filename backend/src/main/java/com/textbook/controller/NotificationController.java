package com.textbook.controller;

import com.textbook.service.NotificationService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(notificationService.pageList(page, size, userId));
    }

    @GetMapping("/unread")
    public Result<?> unreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(notificationService.unreadCount(userId));
    }

    @PutMapping("/read/{id}")
    public Result<?> markRead(@PathVariable Long id) {
        notificationService.markRead(id);
        return Result.success("已读");
    }

    @PostMapping("/send")
    public Result<?> send(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long senderId = (Long) request.getAttribute("userId");
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        String type = (String) params.getOrDefault("type", "SYSTEM");
        Object receiverIdObj = params.get("receiverId");
        Long receiverId = receiverIdObj != null ? Long.valueOf(receiverIdObj.toString()) : null;
        notificationService.sendNotice(title, content, type, senderId, receiverId);
        return Result.success("通知发送成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        notificationService.removeById(id);
        return Result.success("删除成功");
    }
}
