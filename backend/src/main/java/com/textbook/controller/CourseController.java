package com.textbook.controller;

import com.textbook.service.CourseService;
import com.textbook.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword, @RequestParam(required = false) Long majorId,
                          @RequestParam(required = false) String grade) {
        return Result.success(courseService.pageList(page, size, keyword, majorId, grade));
    }

    @GetMapping("/detail/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(courseService.getById(id));
    }

    @GetMapping("/byTeacher")
    public Result<?> byTeacher(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(courseService.getByTeacherId(userId));
    }

    @GetMapping("/all")
    public Result<?> all(@RequestParam(required = false) Long majorId) {
        if (majorId != null) {
            return Result.success(courseService.lambdaQuery().eq(com.textbook.entity.Course::getMajorId, majorId).list());
        }
        return Result.success(courseService.list());
    }
}
