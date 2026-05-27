package com.textbook.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.textbook.entity.Course;
import com.textbook.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {

    public IPage<Course> pageList(int page, int size, String keyword, Long majorId, String grade) {
        return baseMapper.selectCoursePage(new Page<>(page, size), keyword, majorId, grade);
    }

    public List<Course> getByTeacherId(Long teacherId) {
        return baseMapper.selectByTeacherId(teacherId);
    }
}
