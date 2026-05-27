package com.textbook.controller;

import com.textbook.entity.TeacherRecommend;
import com.textbook.service.NotificationService;
import com.textbook.service.StudyNoteService;
import com.textbook.service.TeacherService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudyNoteService noteService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/recommends")
    public Result<?> myRecommends(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(teacherService.getByTeacherId(userId));
    }

    @GetMapping("/recommends/course/{courseId}")
    public Result<?> byCourse(@PathVariable Long courseId) {
        return Result.success(teacherService.getByCourseId(courseId));
    }

    @PostMapping("/recommend")
    public Result<?> addRecommend(@RequestBody TeacherRecommend recommend, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        recommend.setTeacherId(userId);
        return teacherService.addRecommend(recommend);
    }

    @DeleteMapping("/recommend/{id}")
    public Result<?> deleteRecommend(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return teacherService.deleteRecommend(id, userId);
    }

    @PostMapping("/recommendNote/{noteId}")
    public Result<?> recommendNote(@PathVariable Long noteId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return noteService.recommend(noteId, userId);
    }

    @PostMapping("/rejectNote/{noteId}")
    public Result<?> rejectNote(@PathVariable Long noteId, @RequestBody Map<String, String> params,
                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String reason = params.getOrDefault("reason", "审核未通过");
        return noteService.rejectNote(noteId, reason, userId);
    }

    @PostMapping("/publishNotice")
    public Result<?> publishNotice(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long teacherId = (Long) request.getAttribute("userId");
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        Object courseIdObj = params.get("courseId");
        Long courseId = courseIdObj != null ? Long.valueOf(courseIdObj.toString()) : null;
        if (title == null || title.trim().isEmpty()) return Result.error("请输入通知标题");
        if (content == null || content.trim().isEmpty()) return Result.error("请输入通知内容");
        notificationService.broadcastByCourse(title.trim(), content.trim(), teacherId, courseId);
        return Result.success("通知发布成功");
    }
}
