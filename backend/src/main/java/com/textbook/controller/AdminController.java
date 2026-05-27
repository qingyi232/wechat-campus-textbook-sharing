package com.textbook.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.textbook.entity.*;
import com.textbook.mapper.TextbookReportMapper;
import com.textbook.mapper.UserFeedbackMapper;
import com.textbook.service.*;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TextbookService textbookService;
    @Autowired
    private StudyNoteService noteService;
    @Autowired
    private UserService userService;
    @Autowired
    private TextbookReportMapper reportMapper;
    @Autowired
    private UserFeedbackMapper feedbackMapper;

    @GetMapping("/statistics")
    @Cacheable(value = "statistics", key = "'dashboard'", unless = "#result == null")
    public Result<?> statistics() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userService.count(new LambdaQueryWrapper<User>().eq(User::getDeleted, 0)));
        data.put("studentCount", userService.count(new LambdaQueryWrapper<User>().eq(User::getRole, "STUDENT").eq(User::getStatus, 1).eq(User::getDeleted, 0)));
        data.put("teacherCount", userService.count(new LambdaQueryWrapper<User>().eq(User::getRole, "TEACHER").eq(User::getStatus, 1).eq(User::getDeleted, 0)));
        data.put("textbookCount", textbookService.count(new LambdaQueryWrapper<Textbook>().eq(Textbook::getDeleted, 0)));
        data.put("textbookOnSale", textbookService.count(new LambdaQueryWrapper<Textbook>().eq(Textbook::getStatus, "ON_SALE").eq(Textbook::getDeleted, 0)));
        data.put("textbookSold", textbookService.count(new LambdaQueryWrapper<Textbook>().eq(Textbook::getStatus, "SOLD").eq(Textbook::getDeleted, 0)));
        data.put("noteCount", noteService.count(new LambdaQueryWrapper<StudyNote>().eq(StudyNote::getDeleted, 0)));
        data.put("notePublished", noteService.count(new LambdaQueryWrapper<StudyNote>().eq(StudyNote::getStatus, "PUBLISHED").eq(StudyNote::getDeleted, 0)));
        data.put("noteReviewing", noteService.count(new LambdaQueryWrapper<StudyNote>().eq(StudyNote::getStatus, "REVIEWING").eq(StudyNote::getDeleted, 0)));
        data.put("pendingReports", reportMapper.selectCount(new LambdaQueryWrapper<TextbookReport>().eq(TextbookReport::getStatus, "PENDING")));
        data.put("pendingFeedback", feedbackMapper.selectCount(new LambdaQueryWrapper<UserFeedback>().eq(UserFeedback::getStatus, "PENDING")));
        return Result.success(data);
    }

    @GetMapping("/reports")
    public Result<?> reports(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String status) {
        LambdaQueryWrapper<TextbookReport> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) wrapper.eq(TextbookReport::getStatus, status);
        wrapper.orderByDesc(TextbookReport::getCreateTime);
        IPage<TextbookReport> result = reportMapper.selectPage(new Page<>(page, size), wrapper);
        result.getRecords().forEach(r -> {
            User reporter = userService.getById(r.getReporterId());
            if (reporter != null) r.setReporterName(reporter.getRealName());
            Textbook textbook = textbookService.getById(r.getTextbookId());
            if (textbook != null) r.setTextbookTitle(textbook.getTitle());
        });
        return Result.success(result);
    }

    @PutMapping("/report/handle")
    public Result<?> handleReport(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long handlerId = (Long) request.getAttribute("userId");
        Long id = Long.valueOf(params.get("id").toString());
        String result = (String) params.get("result");
        String status = (String) params.get("status");
        TextbookReport report = reportMapper.selectById(id);
        if (report == null) return Result.error("举报不存在");
        report.setStatus(status);
        report.setHandleResult(result);
        report.setHandlerId(handlerId);
        report.setHandleTime(LocalDateTime.now());
        reportMapper.updateById(report);
        return Result.success("处理完成");
    }

    @GetMapping("/feedback")
    public Result<?> feedback(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String status) {
        LambdaQueryWrapper<UserFeedback> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) wrapper.eq(UserFeedback::getStatus, status);
        wrapper.orderByDesc(UserFeedback::getCreateTime);
        return Result.success(feedbackMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @PutMapping("/feedback/reply")
    public Result<?> replyFeedback(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String reply = (String) params.get("reply");
        UserFeedback fb = feedbackMapper.selectById(id);
        if (fb == null) return Result.error("反馈不存在");
        fb.setReply(reply);
        fb.setStatus("REPLIED");
        fb.setReplyTime(LocalDateTime.now());
        feedbackMapper.updateById(fb);
        return Result.success("回复成功");
    }
}
