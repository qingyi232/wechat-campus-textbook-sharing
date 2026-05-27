package com.textbook.controller;

import com.textbook.entity.UserFeedback;
import com.textbook.mapper.UserFeedbackMapper;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private UserFeedbackMapper feedbackMapper;

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody UserFeedback feedback, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        feedback.setUserId(userId);
        feedback.setStatus("PENDING");
        feedbackMapper.insert(feedback);
        return Result.success("反馈提交成功");
    }
}
